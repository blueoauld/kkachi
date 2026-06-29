package com.blueoauld.server.chat.application

import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.chat.application.response.ChatRoomEventResponse
import com.blueoauld.server.chat.application.response.ChatRoomResponse
import com.blueoauld.server.chat.entity.ChatRoom
import com.blueoauld.server.chat.repository.ChatMessageRepository
import com.blueoauld.server.chat.repository.ChatRoomRepository
import com.blueoauld.server.chat.repository.result.ChatRoomResult
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.entity.type.ImageType
import com.blueoauld.server.member.repository.MemberImageRepository
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatRoomService(

    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val memberRepository: MemberRepository,
    private val memberImageRepository: MemberImageRepository,
    private val blockRepository: BlockRepository,
    private val imageStorage: ImageStorage,
    private val chatEventPublisher: ChatEventPublisher,
) {

    companion object {
        private const val EVENT_DESTINATION = "/queue/chat/events"
    }

    @Transactional
    fun createOrGetRoom(memberId: Long, opponentId: Long): ChatRoomResponse {
        if (memberId == opponentId) {
            throw BusinessException(ErrorCode.CANNOT_CHAT_SELF)
        }
        if (!memberRepository.existsById(opponentId)) {
            throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        }
        validateNotBlocked(memberId, opponentId)

        val (member1Id, member2Id) = if (memberId < opponentId) memberId to opponentId else opponentId to memberId
        val room = chatRoomRepository.findByMember1IdAndMember2Id(member1Id, member2Id)
            ?: chatRoomRepository.save(ChatRoom.of(memberId, opponentId))

        return toResponse(room, memberId, opponentId)
    }

    @Transactional(readOnly = true)
    fun getChatRooms(memberId: Long, cursor: Long?, size: Int): CursorResponse<ChatRoomResponse> {
        val results = chatRoomRepository.findChatRooms(memberId, cursor, size + 1)
        return CursorResponse.of(results, size, { it.lastMessageId ?: 0L }) { result ->
            toResponse(result, memberId)
        }
    }

    @Transactional
    fun deleteRoom(memberId: Long, roomId: Long) {
        val room = chatRoomRepository.findByIdOrNull(roomId) ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        if (!room.isMember(memberId)) {
            throw BusinessException(ErrorCode.NOT_CHAT_ROOM_MEMBER)
        }

        val opponentId = room.opponentIdOf(memberId)
        chatRoomRepository.delete(room)

        val event = ChatRoomEventResponse.roomDeleted(roomId)
        chatEventPublisher.sendToUser(memberId, EVENT_DESTINATION, event)
        chatEventPublisher.sendToUser(opponentId, EVENT_DESTINATION, event)
    }

    @Transactional
    fun markAsRead(memberId: Long, roomId: Long) {
        val room = chatRoomRepository.findByIdOrNull(roomId) ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)
        if (!room.isMember(memberId)) {
            throw BusinessException(ErrorCode.NOT_CHAT_ROOM_MEMBER)
        }

        room.lastMessageId?.let { room.markRead(memberId, it) }
    }

    private fun validateNotBlocked(memberId: Long, opponentId: Long) {
        if (blockRepository.existsByBlockerIdAndBlockedId(memberId, opponentId) ||
            blockRepository.existsByBlockerIdAndBlockedId(opponentId, memberId)
        ) {
            throw BusinessException(ErrorCode.CHAT_BLOCKED)
        }
    }

    private fun toResponse(result: ChatRoomResult, memberId: Long): ChatRoomResponse {
        val myLastReadMessageId =
            if (memberId == result.member1Id) result.member1LastReadMessageId else result.member2LastReadMessageId
        val unreadCount = chatMessageRepository.countByRoomIdAndSenderIdAndIdGreaterThan(
            result.roomId,
            result.opponentId,
            myLastReadMessageId,
        )

        return ChatRoomResponse(
            roomId = result.roomId,
            opponentId = result.opponentId,
            profileImageUrl = result.opponentObjectKey?.let { imageStorage.generatePresignedDownloadUrl(it) },
            nickname = result.opponentNickname,
            lastMessageContent = result.lastMessageContent,
            lastMessageType = result.lastMessageType,
            lastMessageAt = result.lastMessageAt,
            unreadCount = unreadCount,
        )
    }

    private fun toResponse(room: ChatRoom, memberId: Long, opponentId: Long): ChatRoomResponse {
        val opponent = memberRepository.findByIdOrNull(opponentId)
            ?: throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        val profileImageKey = memberImageRepository
            .findByMemberIdInAndTypeAndDisplayOrder(listOf(opponentId), ImageType.PUBLIC, 0)
            .firstOrNull()
            ?.objectKey
        val unreadCount = chatMessageRepository.countByRoomIdAndSenderIdAndIdGreaterThan(
            room.id,
            opponentId,
            room.lastReadMessageIdOf(memberId),
        )

        return ChatRoomResponse(
            roomId = room.id,
            opponentId = opponentId,
            profileImageUrl = profileImageKey?.let { imageStorage.generatePresignedDownloadUrl(it) },
            nickname = opponent.nickname,
            lastMessageContent = room.lastMessageContent,
            lastMessageType = room.lastMessageType,
            lastMessageAt = room.lastMessageAt,
            unreadCount = unreadCount,
        )
    }
}
