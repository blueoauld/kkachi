package com.blueoauld.server.member.repository

import com.blueoauld.server.member.entity.Member
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

    fun existsByPhone(phone: String): Boolean

    fun findByPhone(phone: String): Member?

    fun findByNickname(nickname: String): Member?

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

    @Query(
        value = """
            SELECT m.id AS id,
                   m.nickname AS nickname,
                   m.gender AS gender,
                   m.birth_year AS birthYear,
                   m.comment AS comment,
                   CAST(similarity(m.nickname, :keyword) AS double precision) AS similarity
            FROM member m
            WHERE m.id <> :viewerId
              AND m.deleted_at IS NULL
              AND m.nickname % :keyword
              AND NOT EXISTS (
                SELECT 1 FROM block b
                WHERE (b.blocker_id = :viewerId AND b.blocked_id = m.id)
                   OR (b.blocker_id = m.id AND b.blocked_id = :viewerId)
              )
              AND (
                CAST(:cursorSimilarity AS double precision) IS NULL
                OR similarity(m.nickname, :keyword) < :cursorSimilarity
                OR (similarity(m.nickname, :keyword) = :cursorSimilarity AND m.id > :cursorId)
              )
            ORDER BY similarity(m.nickname, :keyword) DESC, m.id
        """,
        nativeQuery = true,
    )
    fun searchByNickname(
        @Param("viewerId") viewerId: Long,
        @Param("keyword") keyword: String,
        @Param("cursorSimilarity") cursorSimilarity: Double?,
        @Param("cursorId") cursorId: Long?,
        pageable: Pageable,
    ): List<MemberSearchProjection>
}

interface MemberDistanceProjection {

    val id: Long
    val distance: Double?
}

interface MemberSearchProjection {

    val id: Long
    val nickname: String
    val gender: String
    val birthYear: Int
    val comment: String
    val similarity: Double
}
