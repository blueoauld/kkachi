package com.blueoauld.server.activity.repository.impl

import com.blueoauld.server.activity.entity.Heart
import com.blueoauld.server.activity.repository.HeartCustomRepository
import com.blueoauld.server.activity.repository.result.ActivityResult
import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.entity.MemberImage
import com.blueoauld.server.member.entity.type.ImageType
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class HeartCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : HeartCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findSentHearts(
        senderId: Long,
        cursorId: Long?,
        size: Int,
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(Heart::id),
                path(Heart::receiverId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(Heart::class),
                join(Member::class).on(
                    path(Heart::receiverId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(Heart::senderId).eq(senderId),
                cursorId?.let {
                    path(Heart::id).lt(it)
                },
            ).orderBy(
                path(Heart::id).desc(),
            )
        }

        val rendered = jpqlRenderer.render(query, jpqlRenderContext)
        val jpaQuery = entityManager.createQuery(rendered.query, ActivityResult::class.java).apply {
            rendered.params.forEach { (name, value) ->
                setParameter(name, value)
            }
        }
        return jpaQuery.setMaxResults(size).resultList
    }

    override fun findReceivedHearts(
        receiverId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(Heart::id),
                path(Heart::senderId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(Heart::class),
                join(Member::class).on(
                    path(Heart::senderId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(Heart::receiverId).eq(receiverId),
                cursorId?.let {
                    path(Heart::id).lt(it)
                },
            ).orderBy(
                path(Heart::id).desc(),
            )
        }

        val rendered = jpqlRenderer.render(query, jpqlRenderContext)
        val jpaQuery = entityManager.createQuery(rendered.query, ActivityResult::class.java).apply {
            rendered.params.forEach { (name, value) ->
                setParameter(name, value)
            }
        }
        return jpaQuery.setMaxResults(size).resultList
    }
}