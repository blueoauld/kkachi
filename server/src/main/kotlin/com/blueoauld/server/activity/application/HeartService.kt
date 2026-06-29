package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.SentHeartResponse
import com.blueoauld.server.activity.entity.Heart
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.member.entity.type.ImageType
import com.blueoauld.server.member.repository.MemberImageRepository
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.time.ZoneId

@Service
class HeartService(

    private val heartRepository: HeartRepository,
    private val memberRepository: MemberRepository,
    private val memberImageRepository: MemberImageRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
        private const val REPRESENTATIVE_IMAGE_ORDER = 0
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
    fun getSentHearts(senderId: Long, cursor: Long?, size: Int): CursorResponse<SentHeartResponse> {
        val hearts = heartRepository.findSentHearts(senderId, cursor, PageRequest.of(0, size + 1))
        val hasNext = hearts.size > size
        val pageHearts = if (hasNext) hearts.take(size) else hearts

        val receiverIds = pageHearts.map { it.receiverId }
        val memberById = memberRepository.findAllById(receiverIds).associateBy { it.id }
        val representativeImageByMemberId = memberImageRepository
            .findByMemberIdInAndTypeAndDisplayOrder(receiverIds, ImageType.PUBLIC, REPRESENTATIVE_IMAGE_ORDER)
            .associateBy { it.memberId }

        val currentYear = Year.now(KST).value
        val items = pageHearts.mapNotNull { heart ->
            val member = memberById[heart.receiverId] ?: return@mapNotNull null

            SentHeartResponse(
                heartId = heart.id,
                memberId = member.id,
                profileImageUrl = representativeImageByMemberId[heart.receiverId]?.let {
                    imageStorage.generatePresignedDownloadUrl(it.objectKey)
                },
                nickname = member.nickname,
                gender = member.gender,
                age = currentYear - member.birthYear,
                comment = member.comment,
            )
        }

        return CursorResponse(
            items = items,
            nextCursor = if (hasNext) pageHearts.last().id else null,
            hasNext = hasNext,
        )
    }
}
