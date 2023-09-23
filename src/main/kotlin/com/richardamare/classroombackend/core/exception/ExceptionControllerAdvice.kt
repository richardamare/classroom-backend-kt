package com.richardamare.classroombackend.core.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
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

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors = ex.bindingResult.fieldErrors.map { it.defaultMessage }
        return ResponseEntity.status(400).body(
            Response(
                message = errors.joinToString(", "),
                statusCode = 400,
            )
        )
    }

    override fun handleServletRequestBindingException(
        ex: ServletRequestBindingException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        return ResponseEntity.status(400).body(
            Response(
                message = getMessage(ex, "Bad request"),
                statusCode = 400,
            )
        )
    }

    @ExceptionHandler(HttpException::class)
    fun httpException(e: HttpException, request: WebRequest): ResponseEntity<Any>? {
        return ResponseEntity.status(e.statusCode).body(
            Response(
                message = getMessage(e, HttpStatus.valueOf(e.statusCode).reasonPhrase),
                statusCode = e.statusCode,
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