package hht.dragon.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-2
 */
@RestController
public class TestController {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("/test1")
    public String test() {
        String key = "testProcess";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(key);
        return instance.getProcessDefinitionId();
    }

    @RequestMapping("/one")
    public String one() {
        String name = "张三";
        List<Task> tasks = taskService
                .createTaskQuery()
                .taskAssignee(name)
                .list();

        return tasks.get(0).getId() + " : " + tasks.get(0).getName();
    }

    @RequestMapping("/two")
    public String two(String id) {
        taskService.complete(id);
        return "完成任务";
    }

    @PostMapping("/file")
    public String file(MultipartFile file, String name) throws IOException {
        System.out.println(file.getSize());
        ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
        Deployment deployment = repositoryService.createDeployment()
                .name(name)
                .addZipInputStream(zipInputStream)
                .deploy();
        return deployment.getId() + " : " + deployment.getName();
    }


}
