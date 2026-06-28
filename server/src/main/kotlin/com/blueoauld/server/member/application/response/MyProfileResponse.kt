package com.blueoauld.server.member.application.response

import com.blueoauld.server.member.entity.type.GenderType

data class MyProfileResponse(

    val memberId: Long,
    val nickname: String,
    val gender: GenderType,
    val age: Int,
    val bio: String,
    val heartCount: Long,
    val publicImageUrls: List<String>,
)
