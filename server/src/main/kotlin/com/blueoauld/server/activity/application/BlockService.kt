package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.BlockResponse
import com.blueoauld.server.activity.entity.Block
import com.blueoauld.server.activity.repository.BlockRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.global.storage.ImageStorage
import com.blueoauld.server.global.util.AgeCalculator
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BlockService(

    private val blockRepository: BlockRepository,
    private val memberRepository: MemberRepository,
    private val imageStorage: ImageStorage
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
        val results = blockRepository.findBlocks(blockerId, cursor, size + 1)
        return CursorResponse.of(results, size, { it.id }) { block ->
            BlockResponse(
                blockId = block.id,
                memberId = block.memberId,
                profileImageUrl = block.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = block.nickname,
                gender = block.gender,
                age = AgeCalculator.fromBirthYear(block.birthYear),
                comment = block.comment
            )
        }
    }
}
