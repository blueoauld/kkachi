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

    // 회원
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    INVALID_BIRTH_YEAR(HttpStatus.BAD_REQUEST, "이용 가능 연령은 19세 이상 50세 이하입니다."),

    // 좋아요
    CANNOT_HEART_SELF(HttpStatus.BAD_REQUEST, "자기 자신에게 좋아요를 보낼 수 없습니다."),
    ALREADY_HEARTED(HttpStatus.CONFLICT, "이미 좋아요를 보낸 회원입니다."),

    // 차단
    CANNOT_BLOCK_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 차단할 수 없습니다."),
    ALREADY_BLOCKED(HttpStatus.CONFLICT, "이미 차단한 회원입니다."),

    // 즐겨찾기
    CANNOT_FAVORITE_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 즐겨찾기할 수 없습니다."),
    ALREADY_FAVORITED(HttpStatus.CONFLICT, "이미 즐겨찾기한 회원입니다."),

    // 비밀 사진 공개
    CANNOT_OPEN_SECRET_IMAGE_TO_SELF(HttpStatus.BAD_REQUEST, "자기 자신에게 비밀 사진을 공개할 수 없습니다."),
    ALREADY_OPENED_SECRET_IMAGE(HttpStatus.CONFLICT, "이미 비밀 사진을 공개한 회원입니다."),
    SECRET_IMAGE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "비밀 사진 열람 권한이 없습니다."),

    // 포인트
    INVALID_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "포인트 금액은 1 이상이어야 합니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트 잔액이 부족합니다."),
    ALREADY_CHECKED_IN(HttpStatus.CONFLICT, "오늘은 이미 출석체크를 했습니다."),

    // 신고
    CANNOT_REPORT_SELF(HttpStatus.BAD_REQUEST, "자기 자신을 신고할 수 없습니다."),

    // 정지
    ACCOUNT_SUSPENDED(HttpStatus.FORBIDDEN, "정지된 계정입니다."),
    INVALID_SUSPENSION_DAYS(HttpStatus.BAD_REQUEST, "정지 일수는 1 이상이어야 합니다."),
    SUSPENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "정지 내역을 찾을 수 없습니다."),

    // 채팅
    CANNOT_CHAT_SELF(HttpStatus.BAD_REQUEST, "자기 자신과 채팅할 수 없습니다."),
    CHAT_BLOCKED(HttpStatus.FORBIDDEN, "차단 관계인 상대와는 채팅할 수 없습니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    NOT_CHAT_ROOM_MEMBER(HttpStatus.FORBIDDEN, "채팅방에 참여하고 있지 않습니다."),

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
