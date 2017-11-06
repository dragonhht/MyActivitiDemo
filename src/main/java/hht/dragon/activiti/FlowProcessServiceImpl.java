package hht.dragon.activiti;

import hht.dragon.activiti.execption.CandidateUserEmptyException;
import hht.dragon.activiti.model.BusinessKeyModel;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程服务实现类.
 * @author: huang
 * Date: 17-11-5
 */
@Service
@Slf4j
public class FlowProcessServiceImpl implements FlowProcessService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void deploy(MultipartFile file, String name) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        repositoryService.createDeployment()
                .name(name)
                .addZipInputStream(zipInputStream)
                .deploy();
    }

    @Override
    public void deploy(String bpmnName, String pngName, String name) {
        repositoryService.createDeployment()
                .name(name)
                .addClasspathResource(bpmnName)
                .addClasspathResource(pngName)
                .deploy();
    }

    @Override
    public void startProcess(String key) {
        runtimeService.startProcessInstanceByKey(key);
    }

    @Override
    public void startProcess(String key, String businessKey) {
        businessKey = key + "." + businessKey;
        runtimeService.startProcessInstanceByKey(key, businessKey);
    }

    @Override
    public void startProcess(String key, String businessKey, Map<String, Object> variable) {
        businessKey = key + "." + businessKey;
        runtimeService.startProcessInstanceByKey(key, businessKey, variable);
    }


    @Override
    public List<Task> getPersonTasks(String id) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(id)
                .orderByTaskCreateTime().desc()
                .list();
        return tasks;
    }

    @Override
    public List<Task> getCandidateUserTasks(String id) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateUser(id)
                .orderByTaskCreateTime().desc()
                .list();
        return tasks;
    }

    @Override
    public void pickupTask(String taskId, String id) {
        taskService.claim(taskId, id);
    }

    @Override
    public void addForCandidateUser(String taskId, String id) {
        taskService.addCandidateUser(taskId, id);
    }

    @Override
    public void addForCandidateUser(String taskId, List<String> ids) {
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                taskService.addCandidateUser(taskId, id);
            }
            return;
        }
        throw new CandidateUserEmptyException();
    }

    @Override
    public void delFroCandidateUser(String taskId, String id) {
        taskService.deleteCandidateUser(taskId, id);
    }

    @Override
    public BusinessKeyModel getBusinessKey(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String businessKey = processInstance.getBusinessKey();
        String[] s = businessKey.split("\\.");
        if (s != null && s.length >1) {
            BusinessKeyModel model = new BusinessKeyModel(s[0], s[1]);
            return model;
        }
        return null;
    }

    @Override
    public void compele(String taskId, Map<String, Object> variable) {
        taskService.complete(taskId, variable);
    }

    @Override
    public boolean isCompeled(String processInstanceId) {
        boolean ok = true;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance != null) {
            ok = false;
        }
        return ok;
    }
}
