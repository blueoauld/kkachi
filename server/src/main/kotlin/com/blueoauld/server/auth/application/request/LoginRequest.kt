package com.blueoauld.server.auth.application.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(

    @field:NotBlank(message = "휴대폰 번호는 필수입니다.")
    val phone: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String,
)
