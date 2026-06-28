package com.blueoauld.server.member.application.response

import com.blueoauld.server.member.entity.type.GenderType
import java.time.Instant

data class MemberProfileResponse(

    val memberId: Long,
    val nickname: String,
    val gender: GenderType,
    val age: Int,
    val heartCount: Long,
    val updatedAt: Instant,
    val distance: Double?,
    val favorited: Boolean,
    val hearted: Boolean,
    val secretImageOpenedByMe: Boolean, // 내가 공개
    val secretImageOpenedToMe: Boolean, // 상대가 공개
    val secretImageCount: Long,
    val blocked: Boolean,
    val publicImageUrls: List<String>,
)
