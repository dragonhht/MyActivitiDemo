package com.github.dragonhht.activiti.listents

import org.activiti.engine.delegate.DelegateExecution
import org.activiti.engine.delegate.DelegateTask
import org.activiti.engine.delegate.ExecutionListener
import org.activiti.engine.delegate.TaskListener

/**
 * 会签任务结束监听.
 *
 * @author: huang
 * @Date: 2019-4-7
 */
class SignProcessEndListener: ExecutionListener {
    override fun notify(delegateExecution: DelegateExecution?) {
        println(delegateExecution!!.getVariable("parentProcessInstanceId"))
    }
}