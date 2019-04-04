package com.github.dragonhht.activiti.commands


import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext
import org.activiti.engine.impl.util.ProcessDefinitionUtil

/**
 * 自由跳转节点.
 *
 * @author: huang
 * @Date: 2019-4-1
 */
class FreeJumpCommand constructor(var taskId: String, var nodeId: String, var variables: Map<String, Any>): Command<Unit> {

    override fun execute(commandContext: CommandContext ): Unit? {
        val executionEntityManager = commandContext.executionEntityManager
        val taskEntityManager = commandContext.taskEntityManager
        // 获取当前任务的来源任务及来源节点信息
        val taskEntity = taskEntityManager.findById(this.taskId)
        val executionEntity = executionEntityManager.findById(taskEntity.executionId)
        val process = ProcessDefinitionUtil.getProcess(executionEntity.processDefinitionId)
        // 删除当前节点
        taskEntityManager.deleteTask(taskEntity, "jump", false, false)

        // 获取要跳转的目标节点
        val targetFlowElement = process.getFlowElement(this.nodeId)
        executionEntity.currentFlowElement = targetFlowElement
        // 设置参数
        executionEntity.variables = variables
        val agenda = commandContext.agenda
        agenda.planContinueProcessInCompensation(executionEntity)
        return null
    }
}
