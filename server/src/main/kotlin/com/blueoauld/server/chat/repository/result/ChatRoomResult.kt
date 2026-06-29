package com.blueoauld.server.chat.repository.result

import com.blueoauld.server.chat.entity.type.MessageType
import java.time.Instant

data class ChatRoomResult(

    val roomId: Long,
    val member1Id: Long,
    val member1LastReadMessageId: Long,
    val member2LastReadMessageId: Long,
    val opponentId: Long,
    val opponentObjectKey: String?,
    val opponentNickname: String,
    val lastMessageId: Long?,
    val lastMessageContent: String?,
    val lastMessageType: MessageType?,
    val lastMessageAt: Instant?,
)
