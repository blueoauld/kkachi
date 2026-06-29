package com.blueoauld.server.member.repository.impl

import com.blueoauld.server.activity.entity.Block
import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.entity.type.GenderType
import com.blueoauld.server.member.repository.MemberCustomRepository
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class MemberCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : MemberCustomRepository {

    companion object {
        private val DISTANCE_QUERY = """
            SELECT m.*
            FROM member m, member viewer
            WHERE viewer.id = :viewerId
              AND m.id <> :viewerId
              AND m.deleted_at IS NULL
              AND (CAST(:gender AS text) IS NULL OR m.gender = :gender)
              AND NOT EXISTS (
                SELECT 1 FROM block b
                WHERE (b.blocker_id = :viewerId AND b.blocked_id = m.id)
                   OR (b.blocker_id = m.id AND b.blocked_id = :viewerId)
              )
              AND (
                CAST(:cursorId AS bigint) IS NULL
                OR (
                  CAST(:cursorDistance AS double precision) IS NOT NULL AND (
                    m.location IS NULL
                    OR ST_Distance(viewer.location, m.location) > :cursorDistance
                    OR (ST_Distance(viewer.location, m.location) = :cursorDistance AND m.id > :cursorId)
                  )
                )
                OR (
                  CAST(:cursorDistance AS double precision) IS NULL AND m.location IS NULL AND m.id > :cursorId
                )
              )
            ORDER BY (m.location IS NULL), ST_Distance(viewer.location, m.location), m.id 
        """.trimIndent()
    }

    private val jpqlRenderer = JpqlRenderer()

    override fun findRecentMembers(
        viewerId: Long,
        gender: GenderType?,
        cursorUpdatedAt: Instant?,
        cursorId: Long?,
        size: Int,
    ): List<Member> {
        val query = jpql {
            select(
                entity(Member::class),
            ).from(
                entity(Member::class),
            ).whereAnd(
                path(Member::id).ne(viewerId),
                gender?.let { path(Member::gender).eq(it) },
                path(Member::id).notIn(
                    select(path(Block::blockedId))
                        .from(entity(Block::class))
                        .where(path(Block::blockerId).eq(viewerId))
                        .asSubquery()
                ),
                path(Member::id).notIn(
                    select(path(Block::blockerId))
                        .from(entity(Block::class))
                        .where(path(Block::blockedId).eq(viewerId))
                        .asSubquery()
                ),
                cursorUpdatedAt?.let {
                    or(
                        path(Member::updatedAt).lt(it),
                        and(
                            path(Member::updatedAt).eq(it),
                            path(Member::id).lt(cursorId!!),
                        ),
                    )
                },
            ).orderBy(
                path(Member::updatedAt).desc(),
                path(Member::id).desc(),
            )
        }

        val rendered = jpqlRenderer.render(query, jpqlRenderContext)
        val jpaQuery = entityManager.createQuery(rendered.query, Member::class.java).apply {
            rendered.params.forEach { (name, value) ->
                setParameter(name, value)
            }
        }
        return jpaQuery.setMaxResults(size).resultList
    }

    override fun findMembersByDistance(
        viewerId: Long,
        gender: GenderType?,
        cursorDistance: Double?,
        cursorId: Long?,
        size: Int,
    ): List<Member> {
        @Suppress("UNCHECKED_CAST")
        return entityManager.createNativeQuery(DISTANCE_QUERY, Member::class.java).apply {
            setParameter("viewerId", viewerId)
            setParameter("gender", gender?.name)
            setParameter("cursorDistance", cursorDistance)
            setParameter("cursorId", cursorId)
            maxResults = size
        }.resultList as List<Member>
    }
}
