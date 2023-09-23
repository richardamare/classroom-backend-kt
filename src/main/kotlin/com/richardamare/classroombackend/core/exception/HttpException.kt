package com.richardamare.classroombackend.core.exception

abstract class HttpException(message: String) : RuntimeException(message) {
    abstract val statusCode: Int
}