package com.github.dragonhht.activiti.commands

import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext

/**
 * 子流程结束后重置任务处理人.
 *
 * @author: huang
 * @Date: 2019-4-2
 */
class ResetTaskAssignee implements Command<Void> {

    private String taskId

    ResetTaskAssignee(String taskId) {
        this.taskId = taskId
    }

    @Override
    Void execute(CommandContext commandContext) {
        def task = commandContext.getHistoricTaskInstanceEntityManager().findById(taskId)
        if (task.parentTaskId != null) {
            def count = commandContext.getDbSqlSession().createTaskQuery().taskParentTaskId(task.parentTaskId).count()
            // 没有子流程
            if (count < 1) {
                task = commandContext.getDbSqlSession().createTaskQuery().taskId(task.parentTaskId).singleResult()
                def splits = task.assignee.split(":")
                if (splits.size() > 1) {
                    task.assignee = splits[1]
                }
            }
            println count
        }
        return null
    }
}

