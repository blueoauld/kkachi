package com.blueoauld.server.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(

    val status: HttpStatus,
    val message: String,
) {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
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
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),

    // 이미지
    UNSUPPORTED_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
    IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "이미지는 최대 20MB까지 업로드할 수 있습니다."),
    INVALID_IMAGE_OBJECT_KEY(HttpStatus.BAD_REQUEST, "올바르지 않은 이미지 키입니다."),
    IMAGE_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 이미지입니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
}
