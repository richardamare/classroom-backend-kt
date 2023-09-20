package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.identity.params.LoginParams
import com.richardamare.classroombackend.identity.params.UserAdminCreateParams
import com.richardamare.classroombackend.identity.params.UserOfficeCreateParams
import com.richardamare.classroombackend.identity.requests.LoginRequest
import com.richardamare.classroombackend.identity.requests.RegisterAdminRequest
import com.richardamare.classroombackend.identity.requests.UserOfficeCreateRequest
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
        identityService.createAdminUser(
            UserAdminCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                email = body.email,
                password = body.password,
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

    @PostMapping("/register/office")
    fun registerOffice(
        @RequestBody @Valid body: UserOfficeCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {

        identityService.createOfficeUser(
            UserOfficeCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                email = body.email,
                tenantId = tenantId,
            )
        )

        return ResponseEntity.ok(mapOf("message" to "ok"))
    }

}