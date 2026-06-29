package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.FavoriteResponse
import com.blueoauld.server.activity.entity.Favorite
import com.blueoauld.server.activity.repository.FavoriteRepository
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
class FavoriteService(

    private val favoriteRepository: FavoriteRepository,
    private val memberRepository: MemberRepository,
    private val imageStorage: ImageStorage,
) {

    companion object {
        private val KST = ZoneId.of("Asia/Seoul")
    }

    @Transactional
    fun addFavorite(ownerId: Long, targetId: Long) {
        if (ownerId == targetId) {
            throw BusinessException(ErrorCode.CANNOT_FAVORITE_SELF)
        }
        if (!memberRepository.existsById(targetId)) {
            throw BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        }
        if (favoriteRepository.existsByOwnerIdAndTargetId(ownerId, targetId)) {
            throw BusinessException(ErrorCode.ALREADY_FAVORITED)
        }

        favoriteRepository.save(
            Favorite(
                ownerId = ownerId,
                targetId = targetId,
            ),
        )
    }

    @Transactional
    fun removeFavorite(ownerId: Long, targetId: Long) {
        favoriteRepository.deleteByOwnerIdAndTargetId(ownerId, targetId)
    }

    @Transactional(readOnly = true)
    fun getSentFavorites(ownerId: Long, cursor: Long?, size: Int): CursorResponse<FavoriteResponse> {
        val results = favoriteRepository.findSentFavorites(ownerId, cursor, size + 1)
        val hasNext = results.size > size
        val items = if (hasNext) results.dropLast(1) else results
        val nextCursor = if (hasNext) items.last().id else null

        val currentYear = Year.now(KST).value
        val responses = items.map { favorite ->
            FavoriteResponse(
                favoriteId = favorite.id,
                memberId = favorite.memberId,
                profileImageUrl = favorite.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = favorite.nickname,
                gender = favorite.gender,
                age = currentYear - favorite.birthYear,
                comment = favorite.comment
            )
        }

        return CursorResponse(
            items = responses,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }

    @Transactional(readOnly = true)
    fun getReceivedFavorites(targetId: Long, cursor: Long?, size: Int): CursorResponse<FavoriteResponse> {
        val results = favoriteRepository.findReceivedFavorites(targetId, cursor, size + 1)
        val hasNext = results.size > size
        val items = if (hasNext) results.dropLast(1) else results
        val nextCursor = if (hasNext) items.last().id else null

        val currentYear = Year.now(KST).value
        val responses = items.map { favorite ->
            FavoriteResponse(
                favoriteId = favorite.id,
                memberId = favorite.memberId,
                profileImageUrl = favorite.objectKey?.let {
                    imageStorage.generatePresignedDownloadUrl(it)
                },
                nickname = favorite.nickname,
                gender = favorite.gender,
                age = currentYear - favorite.birthYear,
                comment = favorite.comment
            )
        }

        return CursorResponse(
            items = responses,
            nextCursor = nextCursor,
            hasNext = hasNext,
        )
    }
}
