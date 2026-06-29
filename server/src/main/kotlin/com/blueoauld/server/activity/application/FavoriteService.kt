package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.application.response.FavoriteResponse
import com.blueoauld.server.activity.entity.Favorite
import com.blueoauld.server.activity.repository.FavoriteRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.member.application.MemberCardReader
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FavoriteService(

    private val favoriteRepository: FavoriteRepository,
    private val memberRepository: MemberRepository,
    private val memberCardReader: MemberCardReader,
) {

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
        val favorites = favoriteRepository.findFavorites(ownerId, cursor, PageRequest.of(0, size + 1))
        val cardByMemberId = memberCardReader.readByIds(favorites.map { it.targetId })

        return CursorResponse.of(favorites, size, { it.id }) { favorite ->
            cardByMemberId[favorite.targetId]?.let { card ->
                FavoriteResponse(
                    favoriteId = favorite.id,
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
    fun getReceivedFavorites(targetId: Long, cursor: Long?, size: Int): CursorResponse<FavoriteResponse> {
        val favorites = favoriteRepository.findReceivedFavorites(targetId, cursor, PageRequest.of(0, size + 1))
        val cardByMemberId = memberCardReader.readByIds(favorites.map { it.ownerId })

        return CursorResponse.of(favorites, size, { it.id }) { favorite ->
            cardByMemberId[favorite.ownerId]?.let { card ->
                FavoriteResponse(
                    favoriteId = favorite.id,
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
