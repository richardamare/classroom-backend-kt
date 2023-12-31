package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.core.annotation.TenantId
import com.richardamare.classroombackend.identity.params.*
import com.richardamare.classroombackend.identity.request.*
import jakarta.validation.Valid
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

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
        val res = identityService.createOfficeUser(
            UserOfficeCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                email = body.email,
                tenantId = tenantId,
            )
        )

        return ResponseEntity.ok(res)
    }

    @PostMapping("/login/{role}")
    fun loginByRoleAndTenant(
        @RequestBody @Valid body: LoginRequest,
        @TenantId tenantId: String,
        @PathVariable("role") roleParam: String,
    ): ResponseEntity<*> {

        val role = when (roleParam) {
            "office" -> UserRole.OFFICE
            "student" -> UserRole.STUDENT
            "teacher" -> UserRole.TEACHER
            "parent" -> UserRole.PARENT
            else -> throw NotFoundException()
        }

        val res = identityService.login(
            LoginParams(
                email = body.email,
                password = body.password,
                tenantId = tenantId,
                role = role,
            )
        )

        return ResponseEntity.ok(res)
    }

    @PostMapping("/register/teacher")
    fun registerTeacher(
        @RequestBody @Valid body: UserTeacherCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {
        val res = identityService.createTeacherUser(
            UserTeacherCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                tenantId = tenantId,
                email = body.email,
            )
        )

        return ResponseEntity.ok(res)
    }

    @PostMapping("/register/student")
    fun registerStudent(
        @RequestBody @Valid body: UserStudentCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {
        val res = identityService.createStudentUser(
            UserStudentCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                tenantId = tenantId,
                email = body.email,
            )
        )

        return ResponseEntity.ok(res)
    }

    @PostMapping("/register/parent")
    fun registerParent(
        @RequestBody @Valid body: UserParentCreateRequest,
        @TenantId tenantId: String,
    ): ResponseEntity<*> {
        val res = identityService.createParentUser(
            UserParentCreateParams(
                firstName = body.firstName,
                lastName = body.lastName,
                tenantId = tenantId,
                email = body.email,
                studentId = body.studentId,
            )
        )

        return ResponseEntity.ok(res)
    }

    @GetMapping("/user/current")
    fun getCurrentUser(
        principal: Principal
    ): ResponseEntity<*> {
        val res = identityService.getUserById(principal.name)

        return ResponseEntity.ok(res)
    }
}