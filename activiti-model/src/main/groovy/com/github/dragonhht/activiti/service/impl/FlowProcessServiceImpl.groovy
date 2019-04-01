package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.commands.FreeJumpCommand
import com.github.dragonhht.activiti.commands.UpdateHiTaskReasonCommand
import com.github.dragonhht.activiti.service.FlowProcessService
import org.activiti.engine.HistoryService
import org.activiti.engine.ManagementService
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.repository.Deployment
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

import javax.annotation.Resource
import java.util.zip.ZipInputStream

/**
 * .
 *
 * @author: huang* @Date: 2019-3-28
 */
@Service
class FlowProcessServiceImpl implements FlowProcessService {

    @Resource
    private RepositoryService repositoryService
    @Resource
    private RuntimeService runtimeService
    @Resource
    private TaskService taskService
    @Autowired
    private ManagementService managementService
    @Autowired
    private HistoryService historyService

    @Override
    Deployment deployByZipInputStream(ZipInputStream input, String name, String key=name) {
        return repositoryService.createDeployment()
                    .name(name)
                    .key(key)
                    .addZipInputStream(input)
                    .deploy()
    }

    private String getBpmnFileName(String fileName) {
        if (!fileName.endsWithAny('.bpmn', '.BPMN')) {
            return fileName + '.bpmn'
        }
        return fileName
    }

    @Override
    Deployment deployByXmlString(String xml, String name, String fileName, String key=name) {
        fileName = getBpmnFileName(fileName)
        return repositoryService.createDeployment()
                    .name(name)
                    .key(key)
                    .addString(fileName, xml)
                    .deploy()
    }

    @Override
    Deployment deployByBpmnInputStream(InputStream input, String name, String fileName, String key=name) {
        fileName = getBpmnFileName(fileName)
        return repositoryService.createDeployment()
                    .name(name)
                    .key(key)
                    .addInputStream(fileName, input)
                    .deploy()
    }

    @Override
    Deployment deployByClassPathWithImg(String filePath, String pngPath, String name, String key=name) {
        return repositoryService.createDeployment()
                    .name(name)
                    .key(key)
                    .addClasspathResource(filePath)
                    .addClasspathResource(pngPath)
                    .deploy()
    }

    @Override
    Deployment deployByClassPath(String filePath, String name, String key=name) {
        return repositoryService.createDeployment()
                .name(name)
                .key(key)
                .addClasspathResource(filePath)
                .deploy()
    }

    @Override
    void delDeployById(String id) {
        repositoryService.deleteDeployment(id)
    }

    @Override
    ProcessInstance startProcess(String key, Map variables = [:]) {
        return runtimeService.startProcessInstanceByKey(key, variables)
    }

    @Override
    void complete(String taskId, Map variables = [:]) {
        taskService.complete(taskId, variables)
        managementService.executeCommand(new UpdateHiTaskReasonCommand(taskId, 'completed'))
    }

    @Override
    List<Task> getTodoTasks(String userId) {
        return taskService.createTaskQuery()
                .taskAssignee(userId)
                .list()
    }

    @Override
    Object getVariable(String taskId, String key) {
        return taskService.getVariable(taskId, key)
    }

    @Override
    Map<String, Object> getVariables(String taskId) {
        return taskService.getVariables(taskId)
    }

    @Override
    void setAssign(String taskId, String assignId) {
        taskService.setAssignee(taskId, assignId)
    }

    @Override
    void jumpToNode(String taskId, String nodeId, Map<String, Object> variables = [:]) {
        managementService.executeCommand(new FreeJumpCommand(taskId, nodeId, variables))
    }

    @Override
    void jumpToNode(Task task, String nodeId, Map<String, Object> variables = [:]) {
        def taskId = task.id
        managementService.executeCommand(new FreeJumpCommand(task, nodeId, variables))
        setBackTaskDealer(task.processInstanceId, nodeId)
    }

    @Override
    void setBackTaskDealer(String processInstanceId, String definitionKey) {
        def list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(definitionKey)
                .taskDeleteReason("completed")
                .orderByTaskCreateTime().desc()
                .list()
        def historicTask = null;
        if (list != null && list.size() > 0) {
            historicTask = list.get(0)
            // 查询回退后的节点正在运行的任务
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .taskDefinitionKey(definitionKey)
                    .active()
                    .list()
            // 同一节点下有多个任务，则认定为会签任务
            if (taskList != null && taskList.size() > 1) {
                for (int i = 0; i < taskList.size(); i++) {
                    // 设置会签任务处理人（处理人顺序不管）
                    taskService.setAssignee(taskList.get(i).getId(), list.get(i).getAssignee())
                }
            } else {
                def taskNew = taskList.get(0)
                // 顺序会签流程变量处理人
                String variable = (String) runtimeService.getVariable(taskNew.getExecutionId(), "countersign")
                if (!StringUtils.isEmpty(variable)) {
                    // 设置下个顺序会签处理人
                    setAssign(taskNew.getId(), variable)
                } else {
                    // 设置一般回退任务处理人
                    taskService.setAssignee(taskNew.getId(), historicTask.getAssignee())
                }
            }
        }
    }
}
