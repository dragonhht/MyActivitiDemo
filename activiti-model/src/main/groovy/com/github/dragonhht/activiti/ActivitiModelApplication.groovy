package com.github.dragonhht.activiti

import com.github.dragonhht.activiti.i18n.MyLocaleResolver
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.LocaleResolver

@SpringBootApplication
class ActivitiModelApplication {

    static void main(String[] args) {
        SpringApplication.run(ActivitiModelApplication, args)
    }

    @Bean
    LocaleResolver localeResolver() {
        return new MyLocaleResolver()
    }
}
