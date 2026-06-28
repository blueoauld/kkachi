package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.entity.Block
import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BlockService(

    private val blockRepository: BlockRepository,
    private val memberRepository: MemberRepository,
) {

    @Transactional
    fun block(blockerId: Long, blockedId: Long) {
        if (blockerId == blockedId) {
            throw BusinessException(ErrorCode.CANNOT_BLOCK_SELF)
        }
        if (!memberRepository.existsById(blockedId)) {
            throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        }
        if (blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw BusinessException(ErrorCode.ALREADY_BLOCKED)
        }

        blockRepository.save(
            Block(
                blocker = memberRepository.getReferenceById(blockerId),
                blocked = memberRepository.getReferenceById(blockedId),
            ),
        )
    }

    @Transactional
    fun unblock(blockerId: Long, blockedId: Long) {
        blockRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId)
    }
}
