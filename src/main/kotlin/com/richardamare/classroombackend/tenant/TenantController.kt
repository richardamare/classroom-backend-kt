package com.richardamare.classroombackend.tenant

import com.richardamare.classroombackend.tenant.params.TenantCreateParams
import com.richardamare.classroombackend.tenant.params.TenantListParams
import com.richardamare.classroombackend.tenant.request.TenantCreateRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/v1/tenants")
class TenantController(
    private val tenantService: TenantService,
) {
    @PostMapping
    fun createTenant(
        @RequestBody @Valid body: TenantCreateRequest,
        principal: Principal,
    ): ResponseEntity<*> {
        val tenantId = tenantService.createTenant(
            TenantCreateParams(
                name = body.name,
                slug = body.slug,
                ownerId = principal.name,
            )
        )

        return ResponseEntity.ok(mapOf("id" to tenantId))
    }

    @GetMapping
    fun listTenants(
        principal: Principal,
    ): ResponseEntity<*> {
        val tenants = tenantService.listTenants(
            TenantListParams(
                ownerId = principal.name,
            )
        )

        return ResponseEntity.ok(tenants)
    }
}