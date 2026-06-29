package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.BlockResponse
import com.blueoauld.server.activity.entity.Block
import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.member.application.MemberCardReader
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BlockService(

    private val blockRepository: BlockRepository,
    private val memberRepository: MemberRepository,
    private val memberCardReader: MemberCardReader,
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
                blockerId = blockerId,
                blockedId = blockedId,
            ),
        )
    }

    @Transactional
    fun unblock(blockerId: Long, blockedId: Long) {
        blockRepository.deleteByBlockerIdAndBlockedId(blockerId, blockedId)
    }

    @Transactional(readOnly = true)
    fun getBlocks(blockerId: Long, cursor: Long?, size: Int): CursorResponse<BlockResponse> {
        val blocks = blockRepository.findBlocks(blockerId, cursor, PageRequest.of(0, size + 1))
        val cardByMemberId = memberCardReader.readByIds(blocks.map { it.blockedId })

        return CursorResponse.of(blocks, size, { it.id }) { block ->
            cardByMemberId[block.blockedId]?.let { card ->
                BlockResponse(
                    blockId = block.id,
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
