package com.blueoauld.server.member.application

import com.blueoauld.server.member.entity.type.GenderType

data class MemberCard(

    val memberId: Long,
    val profileImageUrl: String?,
    val nickname: String,
    val gender: GenderType,
    val age: Int,
    val comment: String,
)
