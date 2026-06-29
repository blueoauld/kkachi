package com.blueoauld.server.point.repository

import com.blueoauld.server.point.entity.PointHistory

fun interface PointHistoryCustomRepository {

    fun findHistories(
        memberId: Long,
        cursorId: Long?,
        size: Int,
    ): List<PointHistory>
}
