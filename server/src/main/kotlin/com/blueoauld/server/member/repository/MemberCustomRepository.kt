package com.blueoauld.server.member.repository

import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.entity.type.GenderType
import java.time.Instant

interface MemberCustomRepository {

    fun findRecentMembers(
        viewerId: Long,
        gender: GenderType?,
        cursorUpdatedAt: Instant?,
        cursorId: Long?,
        size: Int,
    ): List<Member>

    fun findMembersByDistance(
        viewerId: Long,
        gender: GenderType?,
        cursorDistance: Double?,
        cursorId: Long?,
        size: Int,
    ): List<Member>
}
