package com.blueoauld.server.member.repository

import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.entity.type.GenderType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface MemberRepository : JpaRepository<Member, Long> {

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
        """
        SELECT m
        FROM Member m
        WHERE m.id <> :viewerId
          AND (:gender IS NULL OR m.gender = :gender)
          AND NOT EXISTS (
            SELECT b.id FROM Block b
            WHERE (b.blockerId = :viewerId AND b.blockedId = m.id)
               OR (b.blockerId = m.id AND b.blockedId = :viewerId)
          )
          AND (
            :cursorUpdatedAt IS NULL
            OR m.updatedAt < :cursorUpdatedAt
            OR (m.updatedAt = :cursorUpdatedAt AND m.id < :cursorId)
          )
        ORDER BY m.updatedAt DESC, m.id DESC
        """,
    )
    fun findMembers(
        @Param("viewerId") viewerId: Long,
        @Param("gender") gender: GenderType?,
        @Param("cursorUpdatedAt") cursorUpdatedAt: Instant?,
        @Param("cursorId") cursorId: Long?,
        pageable: Pageable,
    ): List<Member>

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
