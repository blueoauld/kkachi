package com.blueoauld.server.activity.repository.impl

import com.blueoauld.server.activity.entity.Block
import com.blueoauld.server.activity.repository.BlockCustomRepository
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
class BlockCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : BlockCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findBlocks(
        blockerId: Long,
        cursorId: Long?,
        size: Int,
    ): List<ActivityResult> {
        val query = jpql {
            selectNew<ActivityResult>(
                path(Block::id),
                path(Block::blockedId),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(Member::gender),
                path(Member::birthYear),
                path(Member::comment),
            ).from(
                entity(Block::class),
                join(Member::class).on(
                    path(Block::blockedId).eq(path(Member::id))
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0))
                )
            ).whereAnd(
                path(Block::blockerId).eq(blockerId),
                cursorId?.let {
                    path(Block::id).lt(it)
                },
            ).orderBy(
                path(Block::id).desc(),
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