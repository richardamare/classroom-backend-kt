package com.richardamare.classroombackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class MailerConfig {

    @Bean
    fun mailSender(): JavaMailSender {
        return JavaMailSenderImpl()
    }
}