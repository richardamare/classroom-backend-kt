package com.richardamare.classroombackend.tenant.params

data class TenantCreateParams(
    val name: String,
    val slug: String,
    val ownerId: String,
)
