package com.blueoauld.server.point.application.response

import com.blueoauld.server.point.entity.type.PointTransactionType
import java.time.Instant

data class PointHistoryResponse(

    val historyId: Long,
    val type: PointTransactionType,
    val amount: Long,
    val balanceAfter: Long,
    val description: String,
    val createdAt: Instant,
)
