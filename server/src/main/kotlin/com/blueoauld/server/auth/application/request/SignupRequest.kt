package com.blueoauld.server.auth.application.request

import com.blueoauld.server.member.entity.type.GenderType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class SignupRequest(

    @field:NotBlank(message = "휴대폰 번호는 필수입니다.")
    @field:Pattern(regexp = "^010\\d{8}$", message = "올바르지 않은 휴대폰 번호입니다.")
    val phone: String,

    @field:NotBlank(message = "인증 번호는 필수입니다.")
    val verificationCode: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String,

    @field:NotBlank(message = "비밀번호 확인은 필수입니다.")
    val passwordConfirm: String,

    @field:NotNull(message = "성별은 필수입니다.")
    var gender: GenderType,
)
