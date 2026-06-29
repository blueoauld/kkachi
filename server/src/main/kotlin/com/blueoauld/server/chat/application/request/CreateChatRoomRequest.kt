package com.blueoauld.server.chat.application.request

import jakarta.validation.constraints.NotNull

data class CreateChatRoomRequest(

    @field:NotNull(message = "상대 회원 ID는 필수입니다.")
    var opponentId: Long,
)
