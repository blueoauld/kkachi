package com.blueoauld.server.member.application.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateProfileRequest(

    @field:NotBlank(message = "닉네임은 필수입니다.")
    @field:Size(max = 10, message = "닉네임은 최대 10자입니다.")
    val nickname: String,

    val birthYear: Int,

    @field:Size(max = 1000, message = "자기소개는 최대 1000자입니다.")
    val bio: String,
)
