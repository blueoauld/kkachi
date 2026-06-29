package com.blueoauld.server.activity.repository.impl

import com.blueoauld.server.activity.entity.Favorite
import com.blueoauld.server.activity.repository.FavoriteCustomRepository
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
class FavoriteCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : FavoriteCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findSentFavorites(
        ownerId: Long,
        cursorId: Long?,
        size: Int,
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(Favorite::id),
                path(Favorite::targetId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(Favorite::class),
                join(Member::class).on(
                    path(Favorite::targetId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(Favorite::ownerId).eq(ownerId),
                cursorId?.let {
                    path(Favorite::id).lt(it)
                },
            ).orderBy(
                path(Favorite::id).desc(),
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

    override fun findReceivedFavorites(
        targetId: Long,
        cursorId: Long?,
        size: Int
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(Favorite::id),
                path(Favorite::ownerId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(Favorite::class),
                join(Member::class).on(
                    path(Favorite::ownerId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(Favorite::targetId).eq(targetId),
                cursorId?.let {
                    path(Favorite::id).lt(it)
                },
            ).orderBy(
                path(Favorite::id).desc(),
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