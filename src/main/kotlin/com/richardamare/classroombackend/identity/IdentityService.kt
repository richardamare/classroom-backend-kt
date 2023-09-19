package com.richardamare.classroombackend.identity

import com.richardamare.classroombackend.identity.params.UserCreateParams

interface IdentityService {
    fun createUser(params: UserCreateParams): String
}