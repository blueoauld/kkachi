package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.repository.result.ActivityResult

interface FavoriteCustomRepository {

    fun findSentFavorites(
        ownerId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>

    fun findReceivedFavorites(
        targetId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>
}