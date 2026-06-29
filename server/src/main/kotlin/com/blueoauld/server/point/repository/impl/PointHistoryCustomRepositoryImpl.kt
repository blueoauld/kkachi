package com.blueoauld.server.point.repository.impl

import com.blueoauld.server.point.entity.PointHistory
import com.blueoauld.server.point.repository.PointHistoryCustomRepository
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class PointHistoryCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : PointHistoryCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findHistories(
        memberId: Long,
        cursorId: Long?,
        size: Int,
    ): List<PointHistory> {
        val query = jpql {
            select(
                entity(PointHistory::class),
            ).from(
                entity(PointHistory::class),
            ).whereAnd(
                path(PointHistory::memberId).eq(memberId),
                cursorId?.let {
                    path(PointHistory::id).lt(it)
                },
            ).orderBy(
                path(PointHistory::id).desc(),
            )
        }

        val rendered = jpqlRenderer.render(query, jpqlRenderContext)
        val jpaQuery = entityManager.createQuery(rendered.query, PointHistory::class.java).apply {
            rendered.params.forEach { (name, value) ->
                setParameter(name, value)
            }
        }
        return jpaQuery.setMaxResults(size).resultList
    }
}
