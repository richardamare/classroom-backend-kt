package com.richardamare.classroombackend.tenant.request

import org.hibernate.validator.constraints.Length

data class TenantCreateRequest(
    @field:Length(min = 3, max = 50)
    val name: String,
    @field:Length(min = 3, max = 50)
    val slug: String,
)
