package com.blueoauld.server.chat.application.request

import com.blueoauld.server.chat.entity.type.MessageType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SendMessageRequest(

    @field:NotNull(message = "메시지 타입은 필수입니다.")
    var type: MessageType,

    @field:NotBlank(message = "메시지 내용은 필수입니다.")
    val content: String,
)
