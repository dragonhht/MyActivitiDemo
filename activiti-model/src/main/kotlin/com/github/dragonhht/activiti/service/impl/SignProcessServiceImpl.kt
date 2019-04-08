package com.github.dragonhht.activiti.service.impl

import com.github.dragonhht.activiti.params.SubTaskVariableKeys
import com.github.dragonhht.activiti.params.SystemProcessKeys
import com.github.dragonhht.activiti.service.BaseService
import com.github.dragonhht.activiti.service.FlowProcessService
import com.github.dragonhht.activiti.service.SignProcessService
import com.github.dragonhht.activiti.utils.IDGenerator
import groovy.util.logging.Slf4j
import org.activiti.engine.*
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager
import org.activiti.engine.impl.persistence.entity.TaskEntity
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
    private lateinit var flowProcessService: FlowProcessService

    override fun startSign(assignees: List<String>, taskId: String, isSequential: Boolean, variables: MutableMap<String, Any>) {
        val task = baseService.findTaskById(taskId)
        if (variables != null) {
            variables["sign-isSequential-flag"] = isSequential
        }
        variables[SubTaskVariableKeys.SIGN_PERSONS] = assignees
        variables[SubTaskVariableKeys.PARENT_PROCESS_INSTANCE_ID] = task.processInstanceId

        if(isSequential) {
            val signProcessInstance = runtimeService.startProcessInstanceByKey(SystemProcessKeys.SIGN_SEQUENTIAL, variables)
            setSubInstanceId(task.executionId, signProcessInstance.id)

            val signTask = taskService.createTaskQuery()
                    .processInstanceId(signProcessInstance.id)
                    .singleResult()
            signTask.parentTaskId = taskId
            taskService.saveTask(signTask)
        } else {
            val signProcessInstance = runtimeService.startProcessInstanceByKey(SystemProcessKeys.SIGN_NOT_SEQUENTIAL, variables)
            setSubInstanceId(task.executionId, signProcessInstance.id)
            val signTasks = taskService.createTaskQuery()
                    .processInstanceId(signProcessInstance.id)
                    .list()
            for (signTask in signTasks) {
                signTask.parentTaskId = taskId
                taskService.saveTask(signTask)
            }
        }
        // 将主流程挂起
        val processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.processInstanceId)
                .singleResult()
        if (!processInstance.isSuspended) {
            flowProcessService.suspendProcessInstanceById(task.processInstanceId)
        }
    }

    /**
     * 记录任务下的子流程
     */
    private fun setSubInstanceId(executionId: String, instanceId: String) {
        var subInstanceList = runtimeService.getVariable(executionId, "${executionId}-subInstances")
        var subInstances = mutableListOf<String>(instanceId)
        if (subInstanceList != null) {
            subInstances = subInstanceList as MutableList<String>
            subInstances.add(instanceId)
        }
        runtimeService.setVariable(executionId, "${executionId}-subInstances", subInstances)
    }

    /**
     * 创建新任务并设置任务参数
     */
    private fun createSubTask(assign: String, taskId: String, variables: Map<String, Any>, task: Task) {
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
