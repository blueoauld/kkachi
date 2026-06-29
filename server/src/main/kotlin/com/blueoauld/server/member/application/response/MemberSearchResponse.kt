package com.blueoauld.server.member.application.response

import com.blueoauld.server.member.entity.type.GenderType

data class MemberSearchResponse(

    val memberId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val gender: GenderType,
    val age: Int,
    val heartCount: Long,
    val comment: String,
)
