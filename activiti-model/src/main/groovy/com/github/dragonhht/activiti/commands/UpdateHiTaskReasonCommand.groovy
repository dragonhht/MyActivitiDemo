package com.github.dragonhht.activiti.commands

import lombok.AllArgsConstructor
import lombok.Getter
import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandContext
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity

/**
 * 设置历史表中的deleteReason字段.
 *
 * @author: huang
 * @Date: 2019-4-1
 */
class UpdateHiTaskReasonCommand implements Command<Void> {

    private String taskId
    private String deleteReason

    public UpdateHiTaskReasonCommand(String taskId, String deleteReason) {
        this.taskId = taskId
        this.deleteReason = deleteReason
    }

    @Override
    Void execute(CommandContext commandContext) {
        def historicTaskInstance = commandContext
                .getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, this.taskId)
        if (historicTaskInstance != null) {
            historicTaskInstance.setDeleteReason(deleteReason)
            historicTaskInstance.markEnded(deleteReason);
        }
        return null;
    }
}
