package com.blueoauld.server.activity.application

import com.blueoauld.server.activity.entity.Favorite
import com.blueoauld.server.activity.repository.FavoriteRepository
import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.global.exception.ErrorCode
import com.blueoauld.server.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FavoriteService(

    private val favoriteRepository: FavoriteRepository,
    private val memberRepository: MemberRepository,
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
}
