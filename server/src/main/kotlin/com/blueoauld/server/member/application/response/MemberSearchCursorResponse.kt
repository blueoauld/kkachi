package com.blueoauld.server.member.application.response

data class MemberSearchCursorResponse(

    val items: List<MemberSearchResponse>,
    val nextCursor: String?,
    val hasNext: Boolean,
)
