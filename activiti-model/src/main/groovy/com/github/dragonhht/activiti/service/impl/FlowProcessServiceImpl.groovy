package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.service.FlowProcessService
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.repository.Deployment
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.springframework.stereotype.Service

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
    ProcessInstance startProcess(String key) {
        return runtimeService.startProcessInstanceByKey(key)
    }

    @Override
    void complete(String taskId) {
        taskService.complete(taskId)
    }

    @Override
    List<Task> getTodoTasks(String userId) {
        return taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId)
                .list()
    }
}
