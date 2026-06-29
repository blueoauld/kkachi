package com.blueoauld.server.member.application.response

import com.blueoauld.server.member.entity.type.GenderType
import java.time.Instant

data class MemberListResponse(

    val memberId: Long,
    val profileImageUrl: String?,
    val nickname: String,
    val age: Int,
    val gender: GenderType,
    val heartCount: Long,
    val comment: String,
    val updatedAt: Instant,
    val distance: Double?,
)
