package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.repository.result.ActivityResult

fun interface BlockCustomRepository {

    fun findBlocks(
        blockerId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>
}