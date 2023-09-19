package com.richardamare.classroombackend.tenant

import com.richardamare.classroombackend.tenant.params.TenantCreateParams

interface TenantService {
    fun createTenant(params: TenantCreateParams): String
}