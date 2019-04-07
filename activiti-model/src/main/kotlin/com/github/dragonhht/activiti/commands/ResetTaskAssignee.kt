package com.github.dragonhht.activiti.commands

import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext

/**
 * 子流程结束后重置任务处理人.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
class ResetTaskAssignee constructor(var taskId: String): Command<Unit> {

    override fun execute(commandContext: CommandContext ): Unit? {
        // 从历史任务中获取任务
        val taskInstanceEntity = commandContext.historicTaskInstanceEntityManager.findById(taskId)
        if (taskInstanceEntity.parentTaskId != null) {
            // 查询子任务数量
            val count = commandContext.dbSqlSession
                    .createTaskQuery()
                    .taskParentTaskId(taskInstanceEntity.parentTaskId)
                    .count()
            // 没有子任务
            if (count < 1) {
                val task = commandContext.dbSqlSession
                        .createTaskQuery()
                        .taskId(taskInstanceEntity.parentTaskId)
                        .singleResult()
                val splits = task.assignee.split(":")
                if (splits.size > 1) {
                    task.assignee = splits[1]
                }
            }
        }
        return null
    }
}

