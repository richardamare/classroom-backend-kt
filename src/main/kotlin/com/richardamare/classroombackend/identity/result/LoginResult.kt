package com.richardamare.classroombackend.identity.result

data class LoginResult(
    val accessToken: String,
    val expiresAt: Long,
)
