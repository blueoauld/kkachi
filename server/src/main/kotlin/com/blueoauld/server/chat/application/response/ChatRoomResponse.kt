package com.blueoauld.server.chat.application.response

import com.blueoauld.server.chat.entity.type.MessageType
import java.time.Instant

data class ChatRoomResponse(

    val roomId: Long,
    val opponentId: Long,
    val profileImageUrl: String?,
    val nickname: String,
    val lastMessageContent: String?,
    val lastMessageType: MessageType?,
    val lastMessageAt: Instant?,
    val unreadCount: Long,
)
