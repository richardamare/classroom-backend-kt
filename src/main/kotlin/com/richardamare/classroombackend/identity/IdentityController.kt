package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.LoginParams
import com.richardamare.classroombackend.identity.params.UserCreateParams
import com.richardamare.classroombackend.identity.requests.LoginRequest
import com.richardamare.classroombackend.identity.requests.RegisterAdminRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/identity")
class IdentityController(
    private val identityService: IdentityService,
) {

    @PostMapping("/register/admin")
    fun registerAdmin(
        @RequestBody @Valid body: RegisterAdminRequest,
    ): ResponseEntity<*> {
        identityService.createUser(
            UserCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                email = body.email,
                password = body.password,
                tenantId = null,
                role = UserRole.ADMIN,
            )
        )

        return ResponseEntity.ok(mapOf("message" to "Please, verify your email address"))
    }

    @PostMapping("/login/admin")
    fun loginAdmin(
        @RequestBody @Valid body: LoginRequest,
    ): ResponseEntity<*> {
        val res = identityService.login(
            LoginParams(
                email = body.email,
                password = body.password,
                tenantId = null,
                role = UserRole.ADMIN,
            )
        )

        return ResponseEntity.ok(res)
    }

}