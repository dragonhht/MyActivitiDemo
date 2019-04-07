package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.params.SystemProcessKeys
import com.github.dragonhht.activiti.service.BaseService
import com.github.dragonhht.activiti.service.SignProcessService
import com.github.dragonhht.activiti.utils.IDGenerator
import groovy.util.logging.Slf4j
import org.activiti.engine.RepositoryService
import org.activiti.engine.RuntimeService
import org.activiti.engine.TaskService
import org.activiti.engine.impl.persistence.entity.TaskEntity
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
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
open class SignProcessServiceImpl: SignProcessService {

    @Resource
    private lateinit var taskService: TaskService
    @Resource
    private lateinit var baseService: BaseService
    @Resource
    private lateinit var runtimeService: RuntimeService
    @Resource
    private lateinit var repositoryService: RepositoryService

    override fun startSign(assignees: List<String>, taskId: String, isSequential: Boolean, variables: MutableMap<String, Any>) {
        val task = baseService.findTaskById(taskId)
        if (variables != null) {
            variables["sign-isSequential-flag"] = isSequential
        }
        if(isSequential) {
            var newTaskId: String? = null
            variables["signPersons"] = assignees
            variables["parentProcessInstanceId"] = task.processInstanceId
            val signProcessInstance = runtimeService.startProcessInstanceByKey(SystemProcessKeys.SIGN_SEQUENTIAL, variables)
            val signTask = taskService.createTaskQuery()
                    .processInstanceId(signProcessInstance.id)
                    .singleResult()
            signTask.parentTaskId = taskId
        } else {
            for (assign in assignees) {
                createSubTesk(assign, taskId, variables, task)
            }
        }
        // 将处理人添加会签标志，表示有会签子流程
        taskService.setAssignee(taskId, "sign:${task.assignee}")
        runtimeService.suspendProcessInstanceById(task.processInstanceId)
    }

    /**
     * 创建新任务并设置任务参数
     */
    private fun createSubTesk(assign: String, taskId: String, variables: Map<String, Any>, task: Task) {
        val taskEntity = taskService.newTask(IDGenerator.getId()) as TaskEntity
        taskEntity.assignee = assign
        taskEntity.name = "${task.name}-会签"
        taskEntity.processDefinitionId = task.processDefinitionId
        taskEntity.parentTaskId = taskId
        taskEntity.description = "signProcess"
        taskEntity.variables = variables
        taskService.saveTask(taskEntity)
    }
}
