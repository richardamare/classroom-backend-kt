package com.richardamare.classroombackend.config

import com.richardamare.classroombackend.core.annotation.CurrentUserResolver
import com.richardamare.classroombackend.core.annotation.TenantIdResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    private val tenantIdResolver: TenantIdResolver,
    private val currentUserResolver: CurrentUserResolver,
) : WebMvcConfigurer {
    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.addAll(listOf(currentUserResolver, tenantIdResolver))
    }
}