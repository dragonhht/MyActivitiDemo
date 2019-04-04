package com.github.dragonhht.activiti


import com.github.dragonhht.activiti.i18n.MyLocaleResolver
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
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
    runApplication<ActivitiModelApplication>(*args)
}
