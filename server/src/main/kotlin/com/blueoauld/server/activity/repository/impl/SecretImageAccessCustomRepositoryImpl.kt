package com.blueoauld.server.activity.repository.impl

import com.blueoauld.server.activity.entity.SecretImageAccess
import com.blueoauld.server.activity.repository.SecretImageAccessCustomRepository
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
class SecretImageAccessCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : SecretImageAccessCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findSecretImageViewers(
        ownerId: Long,
        cursorId: Long?,
        size: Int,
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(SecretImageAccess::id),
                path(SecretImageAccess::viewerId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(SecretImageAccess::class),
                join(Member::class).on(
                    path(SecretImageAccess::viewerId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(SecretImageAccess::ownerId).eq(ownerId),
                cursorId?.let {
                    path(SecretImageAccess::id).lt(it)
                },
            ).orderBy(
                path(SecretImageAccess::id).desc(),
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

    override fun findSecretImageOwners(
        viewerId: Long,
        cursorId: Long?,
        size: Int,
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(SecretImageAccess::id),
                path(SecretImageAccess::ownerId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(SecretImageAccess::class),
                join(Member::class).on(
                    path(SecretImageAccess::ownerId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(SecretImageAccess::viewerId).eq(viewerId),
                cursorId?.let {
                    path(SecretImageAccess::id).lt(it)
                },
            ).orderBy(
                path(SecretImageAccess::id).desc(),
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
