package com.blueoauld.server.member.application.response

data class MemberCursorResponse(

    val items: List<MemberListResponse>,
    val nextCursor: String?,
    val hasNext: Boolean,
)
