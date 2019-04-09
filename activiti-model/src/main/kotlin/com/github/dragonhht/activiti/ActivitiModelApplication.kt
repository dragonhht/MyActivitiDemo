package com.github.dragonhht.activiti


import com.github.dragonhht.activiti.i18n.MyLocaleResolver
import com.github.dragonhht.activiti.spring.InitSystemResource
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.LocaleResolver

@SpringBootApplication
open class ActivitiModelApplication {
    @Bean
    open fun localeResolver(): LocaleResolver {
        return MyLocaleResolver()
    }
}

fun main(args: Array<String>) {
    val springApplication = SpringApplication(ActivitiModelApplication::class.java)
    springApplication.addListeners(InitSystemResource())
    springApplication.run(*args)
}
