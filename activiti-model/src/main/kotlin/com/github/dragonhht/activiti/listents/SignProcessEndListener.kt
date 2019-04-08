package com.github.dragonhht.activiti.listents

import com.github.dragonhht.activiti.service.FlowProcessService
import com.github.dragonhht.activiti.utils.SpringContextUtil
import org.activiti.engine.ActivitiException
import org.activiti.engine.delegate.DelegateExecution
import org.activiti.engine.delegate.DelegateTask
import org.activiti.engine.delegate.ExecutionListener
import org.activiti.engine.delegate.TaskListener
import org.activiti.engine.impl.persistence.entity.ExecutionEntity
import org.activiti.engine.impl.persistence.entity.SuspensionState
import org.activiti.engine.runtime.Execution
import org.slf4j.LoggerFactory
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

    private val log = LoggerFactory.getLogger(SignProcessEndListener::class.java)

    override fun notify(delegateExecution: DelegateExecution?) {
        val flowProcessService = SpringContextUtil.getApplicationContext()!!.getBean("flowProcessService") as FlowProcessService
        val parentProcessInstanceId = delegateExecution!!.getVariable("parentProcessInstanceId") as String
        flowProcessService.activeProcessInstanceById(parentProcessInstanceId)
    }
}