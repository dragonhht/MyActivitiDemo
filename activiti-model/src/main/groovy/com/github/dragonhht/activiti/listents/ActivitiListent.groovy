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
class ActivitiListent implements ActivitiEventListener {

    @Override
    void onEvent(ActivitiEvent activitiEvent) {
        if (ActivitiEventType.TASK_COMPLETED == activitiEvent.type) {
            println "ActivitiEventListener: ${activitiEvent.type}"
        }
    }

    @Override
    boolean isFailOnException() {

        return false
    }
}
