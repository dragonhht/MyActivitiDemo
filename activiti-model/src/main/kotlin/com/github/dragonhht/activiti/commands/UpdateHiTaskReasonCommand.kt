package com.github.dragonhht.activiti.commands


import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity

/**
 * 设置历史表中的deleteReason字段.
 *
 * @author: huang
 * @Date: 2019-4-1
 */
class UpdateHiTaskReasonCommand constructor(var taskId: String, var deleteReason: String): Command<Unit> {

    override fun execute(commandContext: CommandContext): Unit? {
        val historicTaskInstance = commandContext
                .dbSqlSession.selectById(HistoricTaskInstanceEntity::class.java, this.taskId)
        if (historicTaskInstance != null) {
            historicTaskInstance.deleteReason = deleteReason
            historicTaskInstance.markEnded(deleteReason)
        }
        return null
    }
}
