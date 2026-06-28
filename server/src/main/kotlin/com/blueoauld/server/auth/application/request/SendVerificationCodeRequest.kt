package com.blueoauld.server.auth.application.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SendVerificationCodeRequest(

    @field:NotBlank(message = "휴대폰 번호는 필수입니다.")
    @field:Pattern(regexp = "^010\\d{8}$", message = "올바르지 않은 휴대폰 번호입니다.")
    val phone: String,
)
