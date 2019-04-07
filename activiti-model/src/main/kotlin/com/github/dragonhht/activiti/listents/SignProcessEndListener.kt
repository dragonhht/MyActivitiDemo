package com.github.dragonhht.activiti.listents

import com.github.dragonhht.activiti.service.FlowProcessService
import com.github.dragonhht.activiti.utils.SpringContextUtil
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.engine.delegate.DelegateTask
import org.activiti.engine.delegate.ExecutionListener
import org.activiti.engine.delegate.TaskListener
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 会签任务结束监听.
 *
 * @author: huang
 * @Date: 2019-4-7
 */
@Component
class SignProcessEndListener: ExecutionListener {

    override fun notify(delegateExecution: DelegateExecution?) {
        val flowProcessService = SpringContextUtil.getApplicationContext()!!.getBean("flowProcessService") as FlowProcessService
        val parentProcessInstanceId = delegateExecution!!.getVariable("parentProcessInstanceId") as String
        flowProcessService.activeProcessInstanceById(parentProcessInstanceId)
    }
}