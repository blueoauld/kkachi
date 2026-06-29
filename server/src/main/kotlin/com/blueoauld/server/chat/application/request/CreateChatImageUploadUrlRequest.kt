package com.blueoauld.server.chat.application.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class CreateChatImageUploadUrlRequest(

    @field:NotBlank(message = "컨텐츠 타입은 필수입니다.")
    val contentType: String,

    @field:Positive(message = "이미지 크기는 0보다 커야 합니다.")
    val contentLength: Long,
)
