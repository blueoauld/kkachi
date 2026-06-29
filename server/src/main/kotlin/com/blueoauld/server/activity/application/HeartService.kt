package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.SentHeartResponse
import com.blueoauld.server.activity.entity.Heart
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.member.application.MemberCardReader
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HeartService(

    private val heartRepository: HeartRepository,
    private val memberRepository: MemberRepository,
    private val memberCardReader: MemberCardReader,
) {

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
        val cardByMemberId = memberCardReader.readByIds(hearts.map { it.receiverId })

        return CursorResponse.of(hearts, size, { it.id }) { heart ->
            cardByMemberId[heart.receiverId]?.let { card ->
                SentHeartResponse(
                    heartId = heart.id,
                    memberId = card.memberId,
                    profileImageUrl = card.profileImageUrl,
                    nickname = card.nickname,
                    gender = card.gender,
                    age = card.age,
                    comment = card.comment,
                )
            }
        }
    }
}
