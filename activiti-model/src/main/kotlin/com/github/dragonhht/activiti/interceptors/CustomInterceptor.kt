package com.github.dragonhht.activiti.interceptors

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor
import org.activiti.engine.impl.interceptor.Command
import org.activiti.engine.impl.interceptor.CommandConfig
import org.slf4j.LoggerFactory

/**
 * 自定义命令拦截器.
 *
 * @author: huang
 * @Date: 2019-4-11
 */
class CustomInterceptor: AbstractCommandInterceptor() {

    companion object {
        private val logger = LoggerFactory.getLogger(CustomInterceptor::class.java)
    }

    override fun <T : Any?> execute(commandConfig: CommandConfig?, command: Command<T>?): T {
        val start = System.currentTimeMillis()
        try {
            return this.next.execute(commandConfig, command)
        } finally {
            val end = System.currentTimeMillis()
            logger.info("{}：总执行时间：{}ms", command!!::class.java.simpleName, end - start)
        }
    }
}