package com.richardamare.classroombackend.tenant

import com.richardamare.classroombackend.tenant.dto.TenantDTO
import com.richardamare.classroombackend.tenant.params.TenantCreateParams
import com.richardamare.classroombackend.tenant.params.TenantListParams

interface TenantService {
    fun createTenant(params: TenantCreateParams): String
    fun listTenants(params: TenantListParams): List<TenantDTO>
}