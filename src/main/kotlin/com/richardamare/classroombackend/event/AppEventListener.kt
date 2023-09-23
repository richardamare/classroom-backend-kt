package com.richardamare.classroombackend.event

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@Configuration
class AppEventListener {

    private val logger = LoggerFactory.getLogger(AppEventListener::class.java)

    @Async
    @EventListener
    fun tenantCreated(event: AppEvent.TenantCreated) {
        logger.info("received event ${event.eventName}")
    }

    @Async
    @EventListener
    fun emailCredentials(event: AppEvent.EmailCredentials) {
        logger.info("received event ${event.eventName}")
    }

    @Async
    @EventListener
    fun emailWelcome(event: AppEvent.EmailWelcome) {
        logger.info("received event ${event.eventName}")
    }
}