package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.SecretImageAccessResponse
import com.blueoauld.server.activity.entity.SecretImageAccess
import com.blueoauld.server.activity.repository.SecretImageAccessRepository
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
class SecretImageAccessService(

    private val secretImageAccessRepository: SecretImageAccessRepository,
    private val memberRepository: MemberRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
    }

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
        val results = secretImageAccessRepository.findSecretImageViewers(ownerId, cursor, size + 1)
        val hasNext = results.size > size
        val items = if (hasNext) results.dropLast(1) else results
        val nextCursor = if (hasNext) items.last().id else null

        val currentYear = Year.now(KST).value
        val responses = items.map { access ->
            SecretImageAccessResponse(
                accessId = access.id,
                memberId = access.memberId,
                profileImageUrl = access.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = access.nickname,
                gender = access.gender,
                age = currentYear - access.birthYear,
                comment = access.comment
            )
        }

        return CursorResponse(
            items = responses,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }

    @Transactional(readOnly = true)
    fun getSecretImageOwners(viewerId: Long, cursor: Long?, size: Int): CursorResponse<SecretImageAccessResponse> {
        val results = secretImageAccessRepository.findSecretImageOwners(viewerId, cursor, size + 1)
        val hasNext = results.size > size
        val items = if (hasNext) results.dropLast(1) else results
        val nextCursor = if (hasNext) items.last().id else null

        val currentYear = Year.now(KST).value
        val responses = items.map { access ->
            SecretImageAccessResponse(
                accessId = access.id,
                memberId = access.memberId,
                profileImageUrl = access.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = access.nickname,
                gender = access.gender,
                age = currentYear - access.birthYear,
                comment = access.comment
            )
        }

        return CursorResponse(
            items = responses,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }
}
