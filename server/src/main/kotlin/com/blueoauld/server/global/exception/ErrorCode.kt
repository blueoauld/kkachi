package com.blueoauld.server.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(

    val status: HttpStatus,
    val message: String,
) {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 인증
    VERIFICATION_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "인증 번호가 만료되었거나 발송되지 않았습니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증 번호가 일치하지 않습니다."),
    DAILY_SEND_COUNT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "인증 번호 발송 횟수를 초과했습니다."),

    // 회원가입
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    ALREADY_REGISTERED_PHONE(HttpStatus.CONFLICT, "이미 가입된 휴대폰 번호입니다."),

    // 로그인
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "휴대폰 번호 또는 비밀번호가 올바르지 않습니다."),
}
