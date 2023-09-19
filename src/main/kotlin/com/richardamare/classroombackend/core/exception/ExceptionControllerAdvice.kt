package com.richardamare.classroombackend.core.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun notFoundException(e: ResourceNotFoundException, request: WebRequest): ResponseEntity<Any>? {
        val body = ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.message ?: "Resource not found")
        return handleExceptionInternal(e, body, HttpHeaders(), body.statusCode, request)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(e: IllegalStateException, request: WebRequest): ResponseEntity<Any>? {
        val body = ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, e.message ?: "Internal server error")
        return handleExceptionInternal(e, body, HttpHeaders(), body.statusCode, request)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any>? {
        val body = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.message ?: "Bad request")
        return handleExceptionInternal(e, body, HttpHeaders(), body.statusCode, request)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<Any>? {
        val message = e.bindingResult.fieldErrors.joinToString(", ") { it.defaultMessage ?: "" }
        val body = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, message)
        return handleExceptionInternal(e, body, HttpHeaders(), body.statusCode, request)
    }

    @ExceptionHandler(Exception::class)
    fun fallbackException(e: Exception, request: WebRequest): ResponseEntity<Any>? {
        val body = ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, e.message ?: "Internal server error")
        return handleExceptionInternal(e, body, HttpHeaders(), body.statusCode, request)
    }
}