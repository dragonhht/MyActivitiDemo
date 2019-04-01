package com.github.dragonhht.activiti.commands

import org.activiti.bpmn.model.FlowElement
import org.activiti.engine.ActivitiEngineAgenda
import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext
import org.activiti.engine.impl.persistence.entity.ExecutionEntity
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager
import org.activiti.engine.impl.persistence.entity.TaskEntity
import org.activiti.engine.impl.persistence.entity.TaskEntityManager
import org.activiti.engine.impl.util.ProcessDefinitionUtil
import org.activiti.bpmn.model.Process

/**
 * 自由跳转节点.
 *
 * @author: huang
 * @Date: 2019-4-1
 */
class FreeJumpCommand implements Command<Void> {

    private String taskId
    private String nodeId

    public FreeJumpCommand(String taskId, String nodeId) {
        this.taskId = taskId
        this.nodeId = nodeId
    }

    @Override
    Void execute(CommandContext commandContext) {
        def executionEntityManager = commandContext.getExecutionEntityManager()
        def taskEntityManager = commandContext.getTaskEntityManager()
        // 获取当前任务的来源任务及来源节点信息
        def taskEntity = taskEntityManager.findById(this.taskId)
        def executionEntity = executionEntityManager.findById(taskEntity.getExecutionId())
        def process = ProcessDefinitionUtil.getProcess(executionEntity.getProcessDefinitionId())
        // 删除当前节点
        taskEntityManager.deleteTask(taskEntity, "jump", true, true)
        // 获取要跳转的目标节点
        def targetFlowElement = process.getFlowElement(this.nodeId)
        executionEntity.setCurrentFlowElement(targetFlowElement)
        def agenda = commandContext.getAgenda()
        agenda.planContinueProcessInCompensation(executionEntity)
        return null
    }
}
