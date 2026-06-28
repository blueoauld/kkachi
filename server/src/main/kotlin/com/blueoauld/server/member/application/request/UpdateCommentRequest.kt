package com.blueoauld.server.member.application.request

import jakarta.validation.constraints.Size

data class UpdateCommentRequest(

    @field:Size(max = 100, message = "코멘트는 최대 100자입니다.")
    val comment: String,
)
