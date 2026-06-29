package com.blueoauld.server.activity.application.response

import com.blueoauld.server.member.entity.type.GenderType

data class SentFavoriteResponse(

    val favoriteId: Long,
    val memberId: Long,
    val profileImageUrl: String?,
    val nickname: String,
    val gender: GenderType,
    val age: Int,
    val comment: String,
)
