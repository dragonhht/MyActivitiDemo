package com.github.dragonhht.activiti.listents

import com.github.dragonhht.activiti.params.SubTaskVariableKeys
import com.github.dragonhht.activiti.service.BaseService
import com.github.dragonhht.activiti.service.FlowProcessService
import com.github.dragonhht.activiti.utils.SpringContextUtil
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.engine.delegate.ExecutionListener
import org.activiti.engine.impl.persistence.entity.SuspensionState
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 会签任务结束监听.
 *
 * @author: huang
 * @Date: 2019-4-7
 */
@Component
class SignProcessEndListener: ExecutionListener {

    private val log = LoggerFactory.getLogger(SignProcessEndListener::class.java)

    override fun notify(delegateExecution: DelegateExecution?) {
        val activitiBaseService = SpringContextUtil.getApplicationContext()!!
                .getBean("activitiBaseService") as BaseService
        val parentTaskId = delegateExecution!!
                .getVariable(SubTaskVariableKeys.PARENT_TASK_ID) as String
        var task = activitiBaseService.findTaskById(parentTaskId)
        // 激活任务
        if (task.isSuspended)
            activitiBaseService.setTaskSuspensionState(task, SuspensionState.ACTIVE)
    }
}