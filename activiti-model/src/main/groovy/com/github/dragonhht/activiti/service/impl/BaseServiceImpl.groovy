package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.service.BaseService
import org.activiti.engine.RepositoryService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.RepositoryServiceImpl
import org.activiti.engine.repository.ProcessDefinition
import org.activiti.engine.task.Task
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * 流程基础服务实现类.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
@Service
class BaseServiceImpl implements BaseService {

    @Resource
    private TaskService taskService
    @Resource
    private RepositoryService repositoryService

    @Override
    Task findTaskById(String taskId) throws Exception {
        def task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult()
        if (task == null) {
            throw new RuntimeException("任务实例未找到!")
        }
        return task
    }

    @Override
    ProcessDefinition findProcessDefinitionById(String id) throws Exception {
        def processDefinition = ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(id)
        if (processDefinition == null) {
            throw new RuntimeException("流程定义未找到!")
        }
        return processDefinition
    }
}
