package com.blueoauld.server.activity.repository.result

import com.blueoauld.server.member.entity.type.GenderType

data class ActivityResult(

    val id: Long,
    val memberId: Long,
    val objectKey: String?,
    val nickname: String,
    val gender: GenderType,
    val birthYear: Int,
    val comment: String,
)
