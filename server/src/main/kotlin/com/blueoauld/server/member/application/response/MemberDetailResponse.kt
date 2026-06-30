package com.blueoauld.server.member.application.response

import com.blueoauld.server.member.entity.type.GenderType
import java.time.Instant

data class MemberDetailResponse(

    val id: Long,
    val nickname: String,
    val gender: GenderType,
    val birthYear: Int,
    val phone: String,
    val comment: String,
    val bio: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val publicImageUrls: List<String>,
    val secretImageUrls: List<String>,
)
