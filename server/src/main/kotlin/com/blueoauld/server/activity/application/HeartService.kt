package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.HeartResponse
import com.blueoauld.server.activity.entity.Heart
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.time.ZoneId

@Service
class HeartService(

    private val heartRepository: HeartRepository,
    private val memberRepository: MemberRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
    }

    @Transactional
    fun sendHeart(senderId: Long, receiverId: Long) {
        if (senderId == receiverId) {
            throw BusinessException(ErrorCode.CANNOT_HEART_SELF)
        }
        if (!memberRepository.existsById(receiverId)) {
            throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        }
        if (heartRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw BusinessException(ErrorCode.ALREADY_HEARTED)
        }

        heartRepository.save(
            Heart(
                senderId = senderId,
                receiverId = receiverId,
            ),
        )
    }

    @Transactional
    fun cancelHeart(senderId: Long, receiverId: Long) {
        heartRepository.deleteBySenderIdAndReceiverId(senderId, receiverId)
    }

    @Transactional(readOnly = true)
    fun getSentHearts(senderId: Long, cursor: Long?, size: Int): CursorResponse<HeartResponse> {
        val results = heartRepository.findSentHearts(senderId, cursor, size + 1)
        val hasNext = results.size > size
        val items = if (hasNext) results.dropLast(1) else results
        val nextCursor = if (hasNext) items.last().id else null

        val currentYear = Year.now(KST).value
        val responses = items.map { heart ->
            HeartResponse(
                heartId = heart.id,
                memberId = heart.memberId,
                profileImageUrl = heart.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = heart.nickname,
                gender = heart.gender,
                age = currentYear - heart.birthYear,
                comment = heart.comment
            )
        }

        return CursorResponse(
            items = responses,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }

    @Transactional(readOnly = true)
    fun getReceivedHearts(receiverId: Long, cursor: Long?, size: Int): CursorResponse<HeartResponse> {
        val results = heartRepository.findReceivedHearts(receiverId, cursor, size + 1)
        val hasNext = results.size > size
        val items = if (hasNext) results.dropLast(1) else results
        val nextCursor = if (hasNext) items.last().id else null

        val currentYear = Year.now(KST).value
        val responses = items.map { heart ->
            HeartResponse(
                heartId = heart.id,
                memberId = heart.memberId,
                profileImageUrl = heart.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = heart.nickname,
                gender = heart.gender,
                age = currentYear - heart.birthYear,
                comment = heart.comment
            )
        }

        return CursorResponse(
            items = responses,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }
}
