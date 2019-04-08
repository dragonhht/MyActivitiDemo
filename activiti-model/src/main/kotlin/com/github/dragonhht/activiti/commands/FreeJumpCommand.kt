package com.github.dragonhht.activiti.commands


import com.fasterxml.jackson.databind.node.ArrayNode
import org.activiti.engine.impl.HistoricTaskInstanceQueryImpl
import org.activiti.engine.impl.TaskQueryImpl
import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityManager
import org.activiti.engine.impl.persistence.entity.TaskEntityManager
import org.activiti.engine.impl.util.ProcessDefinitionUtil
import org.springframework.util.StringUtils

/**
 * 自由跳转节点.
 *
 * @author: huang
 * @Date: 2019-4-1
 */
class FreeJumpCommand constructor(var taskId: String, var nodeId: String, var variables: Map<String, Any>): Command<Unit> {

    override fun execute(commandContext: CommandContext): Unit? {
        val executionEntityManager = commandContext.executionEntityManager
        val taskEntityManager = commandContext.taskEntityManager
        // 获取当前任务的来源任务及来源节点信息
        val taskEntity = taskEntityManager.findById(this.taskId)
        val executionEntity = executionEntityManager.findById(taskEntity.executionId)
        val process = ProcessDefinitionUtil.getProcess(executionEntity.processDefinitionId)
        // 删除当前节点
        taskEntityManager.deleteTask(taskEntity, "jump", false, false)
        // 删除父当前节点下的所有子任务
        delSubTask(executionEntityManager, taskEntityManager, taskEntity.executionId)
        // 获取要跳转的目标节点
        val targetFlowElement = process.getFlowElement(this.nodeId)
        executionEntity.currentFlowElement = targetFlowElement
        // 设置参数
        executionEntity.variables = variables
        val agenda = commandContext.agenda
        agenda.planContinueProcessInCompensation(executionEntity)
        return null
    }

    /**
     * 删除指定任务下的所有子任务
     */
    private fun delSubTask(executionEntityManager: ExecutionEntityManager, taskEntityManager: TaskEntityManager, parentExecutionId: String?) {
        if (StringUtils.isEmpty(parentExecutionId)) {
            return
        }

        val executionEntity = executionEntityManager.findById(parentExecutionId)
        var subInstanceIdList = executionEntity.getVariable("${parentExecutionId}-subInstances")
        if (subInstanceIdList != null) {
            var subInstanceIds = subInstanceIdList as ArrayNode
            for (subInstanceId in subInstanceIdList) {
                val tasks = taskEntityManager.findTasksByProcessInstanceId(subInstanceId.textValue())
                for (task in tasks) {
                    delSubTask(executionEntityManager, taskEntityManager, task.executionId)
                }
                taskEntityManager.deleteTasksByProcessInstanceId(subInstanceId.textValue(), "jump", true)
            }
        }
    }
}
