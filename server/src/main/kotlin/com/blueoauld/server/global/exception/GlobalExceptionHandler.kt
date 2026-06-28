package com.blueoauld.server.global.exception

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        return ResponseEntity.status(errorCode.status).body(ErrorResponse.of(errorCode))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorCode = ErrorCode.INVALID_INPUT
        val message = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: errorCode.message
        return ResponseEntity.status(errorCode.status).body(ErrorResponse.of(errorCode, message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("처리되지 않은 오류가 발생했습니다.", e)

        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(errorCode.status).body(ErrorResponse.of(errorCode))
    }
}
