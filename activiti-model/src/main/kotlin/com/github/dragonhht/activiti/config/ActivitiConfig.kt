package com.github.dragonhht.activiti.config

import com.github.dragonhht.activiti.listents.ActivitiListent
import org.activiti.engine.delegate.event.ActivitiEventListener
import org.activiti.spring.SpringProcessEngineConfiguration
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

/**
 * Activiti配置.
 *
 * @author: huang
 * @Date: 2019-3-31
 */
@Configuration
open class ActivitiConfig: ProcessEngineConfigurationConfigurer {

    @Autowired
    private lateinit var activitiListent: ActivitiListent

    override fun configure(springProcessEngineConfiguration: SpringProcessEngineConfiguration) {
        val listents = mutableListOf<ActivitiEventListener>()
        listents.add(activitiListent)
        springProcessEngineConfiguration.eventListeners = listents
    }
}
