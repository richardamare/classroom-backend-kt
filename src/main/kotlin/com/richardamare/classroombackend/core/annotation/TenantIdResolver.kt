package com.richardamare.classroombackend.core.annotation

import com.richardamare.classroombackend.tenant.TenantRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class TenantIdResolver(
    private val tenantRepository: TenantRepository,
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        parameter.getParameterAnnotation(TenantId::class.java)?.let {
            return parameter.parameterType.getTypeName() == String::class.java.name
        }
        return false
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): String {

        val request = webRequest.nativeRequest as HttpServletRequest
        val slug = getTenant(request) ?: throw IllegalArgumentException("Invalid tenant id")

        val tenantId = tenantRepository.findBySlug(slug)
            ?: throw IllegalStateException("Tenant not found")

        return tenantId.id.toHexString()
    }

    private fun getTenant(request: HttpServletRequest): String? {
        val domain = request.serverName
        val dot = domain.indexOf(".")
        if (dot == -1) return null
        return domain.substring(0, dot)
    }
}