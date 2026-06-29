package com.blueoauld.server.member.repository

import com.blueoauld.server.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

    fun existsByPhone(phone: String): Boolean

    fun findByPhone(phone: String): Member?

    fun existsByNicknameAndIdNot(nickname: String, id: Long): Boolean

    @Query(
        value = """
            SELECT ST_Distance(viewer.location, target.location)
            FROM member viewer, member target
            WHERE viewer.id = :viewerId AND target.id = :targetId
        """,
        nativeQuery = true,
    )
    fun calculateDistanceMeters(@Param("viewerId") viewerId: Long, @Param("targetId") targetId: Long): Double?

    @Query(
        value = """
            SELECT m.id AS id, ST_Distance(viewer.location, m.location) AS distance
            FROM member m, member viewer
            WHERE viewer.id = :viewerId AND m.id IN (:memberIds)
        """,
        nativeQuery = true,
    )
    fun findDistances(
        @Param("viewerId") viewerId: Long,
        @Param("memberIds") memberIds: Collection<Long>,
    ): List<MemberDistanceProjection>
}

interface MemberDistanceProjection {

    val id: Long
    val distance: Double?
}
