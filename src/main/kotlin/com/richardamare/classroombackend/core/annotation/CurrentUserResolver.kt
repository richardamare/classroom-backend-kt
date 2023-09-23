package com.richardamare.classroombackend.core.annotation

import com.richardamare.classroombackend.auth.AuthUser
import com.richardamare.classroombackend.auth.JwtUtil
import com.richardamare.classroombackend.core.exception.UnauthorizedException
import com.richardamare.classroombackend.identity.UserRepository
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletRequest
import org.bson.types.ObjectId
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserResolver(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
) : HandlerMethodArgumentResolver {
    
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        parameter.getParameterAnnotation(CurrentUser::class.java)?.let {
            return parameter.parameterType.getTypeName() == AuthUser::class.java.name
        }
        return false
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): AuthUser {

        val request = webRequest.nativeRequest as HttpServletRequest

        val jwtToken = run {
            val authHeader = request.getHeader("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw UnauthorizedException()
            }
            authHeader.substring(7)
        }

        val username = try {
            jwtUtil.extractUsername(jwtToken)
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException()
        }

        ObjectId.isValid(username).takeIf { it } ?: throw UnauthorizedException()

        val user = userRepository.findById(ObjectId(username))
            .orElseThrow { UnauthorizedException() }

        return AuthUser(user)
    }
}