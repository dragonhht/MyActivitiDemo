package com.github.dragonhht.activiti.config

import com.github.dragonhht.activiti.listents.ActivitiListent
import org.activiti.spring.SpringProcessEngineConfiguration
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

/**
 * .
 *
 * @author: huang
 * @Date: 2019-3-31
 */
@Configuration
class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Autowired
    private ActivitiListent activitiListent

    @Override
    void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        def listents = new LinkedList()
        listents.add(activitiListent)
        springProcessEngineConfiguration.setEventListeners(listents)
    }
}
