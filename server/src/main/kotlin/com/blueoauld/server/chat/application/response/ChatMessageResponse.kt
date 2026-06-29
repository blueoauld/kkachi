package com.blueoauld.server.chat.application.response

import com.blueoauld.server.chat.entity.ChatMessage
import com.blueoauld.server.chat.entity.type.MessageType
import java.time.Instant

data class ChatMessageResponse(

    val messageId: Long,
    val roomId: Long,
    val senderId: Long,
    val type: MessageType,
    val content: String?,
    val imageUrl: String?,
    val createdAt: Instant,
) {

    companion object {
        fun of(message: ChatMessage, imageUrlResolver: (String) -> String): ChatMessageResponse {
            val isImage = message.type == MessageType.IMAGE
            return ChatMessageResponse(
                messageId = message.id,
                roomId = message.roomId,
                senderId = message.senderId,
                type = message.type,
                content = if (isImage) null else message.content,
                imageUrl = if (isImage) imageUrlResolver(message.content) else null,
                createdAt = message.createdAt,
            )
        }
    }
}
