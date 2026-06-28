package com.blueoauld.server.global.exception

data class ErrorResponse(

    val code: String,
    val message: String,
) {

    companion object {
        fun of(errorCode: ErrorCode) = ErrorResponse(errorCode.name, errorCode.message)

        fun of(errorCode: ErrorCode, message: String) = ErrorResponse(errorCode.name, message)
    }
}
