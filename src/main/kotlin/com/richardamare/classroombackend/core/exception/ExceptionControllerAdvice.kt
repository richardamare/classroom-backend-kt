package com.richardamare.classroombackend.core.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice : ResponseEntityExceptionHandler() {

    private fun getMessage(e: Exception, default: String): String {
        return if (e.message != null && e.message != "") {
            e.message!!
        } else {
            default
        }
    }

    private data class Response(
        val message: String,
        val statusCode: Int,
    )

    @ExceptionHandler(ResourceNotFoundException::class)
    fun notFoundException(e: ResourceNotFoundException, request: WebRequest): ResponseEntity<Any>? {
        this.logger.info("resource not found exception", e)
        return ResponseEntity.status(404).body(
            Response(
                message = getMessage(e, "Resource not found"),
                statusCode = 404,
            )
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(e: IllegalStateException, request: WebRequest): ResponseEntity<Any>? {
        this.logger.info("illegal state exception", e)
        return ResponseEntity.status(500).body(
            Response(
                message = getMessage(e, "Internal server error"),
                statusCode = 500,
            )
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any>? {
        this.logger.info("illegal argument exception", e)
        return ResponseEntity.status(400).body(
            Response(
                message = getMessage(e, "Bad request"),
                statusCode = 400,
            )
        )
    }

//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    fun methodArgumentNotValidException(e: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<Any>? {
//        val message = e.bindingResult.fieldErrors.joinToString(", ") { it.defaultMessage ?: "" }
//        val body = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, message)
//        return handleExceptionInternal(e, body, HttpHeaders(), body.statusCode, request)
//    }

    @ExceptionHandler(UnauthorizedException::class)
    fun unauthorizedException(e: UnauthorizedException, request: WebRequest): ResponseEntity<Any>? {
        this.logger.info("unauthorized exception", e)
        return ResponseEntity.status(401).body(
            Response(
                message = getMessage(e, "Unauthorized"),
                statusCode = 401,
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun fallbackException(e: Exception, request: WebRequest): ResponseEntity<Any>? {
        this.logger.info("unhandled exception", e)
        return ResponseEntity.status(500).body(
            Response(
                message = getMessage(e, "Internal server error"),
                statusCode = 500,
            )
        )
    }
}