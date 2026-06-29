package com.blueoauld.server.chat.application

import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.chat.application.request.CreateChatImageUploadUrlRequest
import com.blueoauld.server.chat.application.request.SendMessageRequest
import com.blueoauld.server.chat.application.response.ChatImageUploadUrlResponse
import com.blueoauld.server.chat.application.response.ChatMessageResponse
import com.blueoauld.server.chat.entity.ChatMessage
import com.blueoauld.server.chat.entity.type.MessageType
import com.blueoauld.server.chat.repository.ChatMessageRepository
import com.blueoauld.server.chat.repository.ChatRoomRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.global.storage.ImageStorage
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChatMessageService(

    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val blockRepository: BlockRepository,
    private val imageStorage: ImageStorage,
    private val chatEventPublisher: ChatEventPublisher,
) {

    companion object {
        private const val MESSAGE_DESTINATION = "/queue/chat/messages"
        private const val MAX_IMAGE_SIZE = 20L * 1024 * 1024 // 20MB
        private const val TMP_KEY_PREFIX = "tmp/chats/"
        private const val IMAGE_PREVIEW = "이미지"
        private const val PREVIEW_MAX_LENGTH = 200

        private val ALLOWED_CONTENT_TYPES = mapOf(
            "image/jpeg" to "jpg",
            "image/png" to "png",
            "image/webp" to "webp",
        )
    }

    fun createImageUploadUrl(memberId: Long, request: CreateChatImageUploadUrlRequest): ChatImageUploadUrlResponse {
        val extension = ALLOWED_CONTENT_TYPES[request.contentType]
            ?: throw BusinessException(ErrorCode.UNSUPPORTED_IMAGE_TYPE)

        if (request.contentLength > MAX_IMAGE_SIZE) {
            throw BusinessException(ErrorCode.IMAGE_SIZE_EXCEEDED)
        }

        val objectKey = "$TMP_KEY_PREFIX$memberId/${UUID.randomUUID()}.$extension"
        val uploadUrl = imageStorage.generatePresignedUploadUrl(objectKey, request.contentType, request.contentLength)

        return ChatImageUploadUrlResponse(objectKey, uploadUrl)
    }

    @Transactional
    fun sendMessage(senderId: Long, roomId: Long, request: SendMessageRequest): ChatMessageResponse {
        val room = chatRoomRepository.findByIdOrNull(roomId) ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        if (!room.isMember(senderId)) {
            throw BusinessException(ErrorCode.NOT_CHAT_ROOM_MEMBER)
        }

        val opponentId = room.opponentIdOf(senderId)
        validateNotBlocked(senderId, opponentId)

        if (request.type == MessageType.IMAGE && !request.content.startsWith("$TMP_KEY_PREFIX$senderId/")) {
            throw BusinessException(ErrorCode.INVALID_IMAGE_OBJECT_KEY)
        }

        val message = chatMessageRepository.save(
            ChatMessage(
                roomId = roomId,
                senderId = senderId,
                type = request.type,
                content = request.content,
            ),
        )

        if (request.type == MessageType.IMAGE) {
            val objectKey = "chat/rooms/$roomId/${message.id}.${request.content.substringAfterLast('.')}"
            imageStorage.copy(request.content, objectKey)
            message.content = objectKey
        }

        val preview = if (request.type == MessageType.IMAGE) IMAGE_PREVIEW else message.content.take(PREVIEW_MAX_LENGTH)
        room.updateLastMessage(message.id, preview, request.type, message.createdAt)
        room.markRead(senderId, message.id)

        val response = ChatMessageResponse.of(message, imageStorage::generatePresignedDownloadUrl)
        chatEventPublisher.sendToUser(opponentId, MESSAGE_DESTINATION, response)
        return response
    }

    @Transactional(readOnly = true)
    fun getMessages(memberId: Long, roomId: Long, cursor: Long?, size: Int): CursorResponse<ChatMessageResponse> {
        val room = chatRoomRepository.findByIdOrNull(roomId) ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        if (!room.isMember(memberId)) {
            throw BusinessException(ErrorCode.NOT_CHAT_ROOM_MEMBER)
        }

        val messages = chatMessageRepository.findMessages(roomId, cursor, size + 1)
        return CursorResponse.of(messages, size, { it.id }) { message ->
            ChatMessageResponse.of(message, imageStorage::generatePresignedDownloadUrl)
        }
    }

    private fun validateNotBlocked(memberId: Long, opponentId: Long) {
        if (blockRepository.existsByBlockerIdAndBlockedId(memberId, opponentId) ||
            blockRepository.existsByBlockerIdAndBlockedId(opponentId, memberId)
        ) {
            throw BusinessException(ErrorCode.CHAT_BLOCKED)
        }
    }
}
