package com.richardamare.classroombackend.tenant.requests

import org.hibernate.validator.constraints.Length

data class TenantCreateRequest(
    @field:Length(min = 3, max = 50)
    val name: String,
    @field:Length(min = 3, max = 50)
    val slug: String,
)
