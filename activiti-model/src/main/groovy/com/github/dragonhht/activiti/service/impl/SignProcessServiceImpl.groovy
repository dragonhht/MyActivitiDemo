package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.service.BaseService
import com.github.dragonhht.activiti.service.SignProcessService
import com.github.dragonhht.activiti.utils.IDGenerator
import groovy.util.logging.Slf4j
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.persistence.entity.TaskEntity
import org.springframework.stereotype.Service

import javax.annotation.Resource

/**
 * 会签服务实现类.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
@Slf4j
@Service
class SignProcessServiceImpl implements SignProcessService {

    @Resource
    private TaskService taskService
    @Resource
    private BaseService baseService
    @Resource
    private RuntimeService runtimeService

    @Override
    void startSign(List<String> assignees, String taskId, Map<String, Object> variables = [:]) {
        def task = baseService.findTaskById(taskId)
        for (assign in assignees) {
            def taskEntity = taskService.newTask(IDGenerator.id) as TaskEntity
            taskEntity.assignee = assign
            taskEntity.name = "${task.name}-会签"
            taskEntity.processDefinitionId = task.processDefinitionId
            taskEntity.parentTaskId = taskId
            taskEntity.description = "signProcess"
            taskEntity.variables = variables
            taskService.saveTask(taskEntity)
        }
        // 将处理人添加会签标志，表示有会签子流程
        taskService.setAssignee(taskId, "sign:${task.assignee}")
    }
}
