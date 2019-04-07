package com.github.dragonhht.activiti.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * 获取Spring容器.
 *
 * @author: huang
 * @Date: 2019-4-7
 */
@Component
class SpringContextUtil: ApplicationContextAware {

    companion object {
        private var applicationContext: ApplicationContext? = null

        fun getApplicationContext(): ApplicationContext? {
            return applicationContext
        }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringContextUtil.applicationContext = applicationContext
    }
}