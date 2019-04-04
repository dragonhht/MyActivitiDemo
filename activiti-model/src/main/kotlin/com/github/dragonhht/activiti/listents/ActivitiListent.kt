package com.github.dragonhht.activiti.listents


import org.activiti.engine.delegate.event.ActivitiEvent
import org.activiti.engine.delegate.event.ActivitiEventListener
import org.activiti.engine.delegate.event.ActivitiEventType
import org.springframework.stereotype.Component

/**
 * 自定义Activiti全局监听器.
 *
 * @author: huang
 * @Date: 2019-3-31
 */
@Component
open class ActivitiListent: ActivitiEventListener {

    override fun onEvent(activitiEvent: ActivitiEvent) {
        if (ActivitiEventType.TASK_COMPLETED == activitiEvent.type) {
            println("ActivitiEventListener: ${activitiEvent.type}")
        }
    }

    override fun isFailOnException(): Boolean {
        return false
    }
}
