package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.entity.Heart
import com.blueoauld.server.activity.repository.HeartRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HeartService(

    private val heartRepository: HeartRepository,
    private val memberRepository: MemberRepository,
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
}
