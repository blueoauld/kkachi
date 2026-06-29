package com.blueoauld.server.activity.repository

import com.blueoauld.server.activity.repository.result.ActivityResult

interface HeartCustomRepository {

    fun findSentHearts(
        senderId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>

    fun findReceivedHearts(
        receiverId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult>
}