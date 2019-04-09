package com.github.dragonhht.activiti.spring

import com.github.dragonhht.activiti.service.FlowProcessService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

/**
 * Spring框架加载完成后，加载指定的Activiti流程资源.
 *
 * @author: huang
 * @Date: 2019-4-7
 */
class InitSystemResource: ApplicationListener<ContextRefreshedEvent> {

    private val log = LoggerFactory.getLogger(InitSystemResource::class.java)

    override fun onApplicationEvent(contextRefreshedEvent: ContextRefreshedEvent) {
        log.info("加载Activiti系统流程资源....")
        val flowProcessService = contextRefreshedEvent.applicationContext
                .getBean("flowProcessService") as FlowProcessService
        // 会签串行
        flowProcessService.deployByClassPath("processes/system/sign_sequential.bpmn", "sign_sequential")
        // 会签并行
        flowProcessService.deployByClassPath("processes/system/sign_not_sequential.bpmn", "sign_not_sequential")
    }
}