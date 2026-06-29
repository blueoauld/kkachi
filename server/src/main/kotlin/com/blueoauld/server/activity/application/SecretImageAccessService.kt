package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.SecretImageAccessResponse
import com.blueoauld.server.activity.entity.SecretImageAccess
import com.blueoauld.server.activity.repository.SecretImageAccessRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.member.application.MemberCardReader
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SecretImageAccessService(

    private val secretImageAccessRepository: SecretImageAccessRepository,
    private val memberRepository: MemberRepository,
    private val memberCardReader: MemberCardReader,
) {

    @Transactional
    fun openSecretImage(ownerId: Long, viewerId: Long) {
        if (ownerId == viewerId) {
            throw BusinessException(ErrorCode.CANNOT_OPEN_SECRET_IMAGE_TO_SELF)
        }
        if (!memberRepository.existsById(viewerId)) {
            throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        }
        if (secretImageAccessRepository.existsByOwnerIdAndViewerId(ownerId, viewerId)) {
            throw BusinessException(ErrorCode.ALREADY_OPENED_SECRET_IMAGE)
        }

        secretImageAccessRepository.save(
            SecretImageAccess(
                ownerId = ownerId,
                viewerId = viewerId,
            ),
        )
    }

    @Transactional
    fun closeSecretImage(ownerId: Long, viewerId: Long) {
        secretImageAccessRepository.deleteByOwnerIdAndViewerId(ownerId, viewerId)
    }

    @Transactional(readOnly = true)
    fun getSecretImageViewers(ownerId: Long, cursor: Long?, size: Int): CursorResponse<SecretImageAccessResponse> {
        val accesses = secretImageAccessRepository.findByOwnerId(ownerId, cursor, PageRequest.of(0, size + 1))
        val cardByMemberId = memberCardReader.readByIds(accesses.map { it.viewerId })

        return CursorResponse.of(accesses, size, { it.id }) { access ->
            cardByMemberId[access.viewerId]?.let { card ->
                SecretImageAccessResponse(
                    accessId = access.id,
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

    @Transactional(readOnly = true)
    fun getSecretImageOwners(viewerId: Long, cursor: Long?, size: Int): CursorResponse<SecretImageAccessResponse> {
        val accesses = secretImageAccessRepository.findByViewerId(viewerId, cursor, PageRequest.of(0, size + 1))
        val cardByMemberId = memberCardReader.readByIds(accesses.map { it.ownerId })

        return CursorResponse.of(accesses, size, { it.id }) { access ->
            cardByMemberId[access.ownerId]?.let { card ->
                SecretImageAccessResponse(
                    accessId = access.id,
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
