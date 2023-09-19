package com.richardamare.classroombackend.identity

enum class UserRole {
    ADMIN,
    TEACHER,
    STUDENT,
    OFFICE,
    PARENT;

    override fun toString(): String {
        return name.lowercase()
    }
}