package com.blueoauld.server.chat.repository.impl

import com.blueoauld.server.chat.entity.ChatMessage
import com.blueoauld.server.chat.repository.ChatMessageCustomRepository
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class ChatMessageCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : ChatMessageCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findMessages(roomId: Long, cursorId: Long?, size: Int): List<ChatMessage> {
        val query = jpql {
            select(
                entity(ChatMessage::class),
            ).from(
                entity(ChatMessage::class),
            ).whereAnd(
                path(ChatMessage::roomId).eq(roomId),
                cursorId?.let {
                    path(ChatMessage::id).lt(it)
                },
            ).orderBy(
                path(ChatMessage::id).desc(),
            )
        }

        val rendered = jpqlRenderer.render(query, jpqlRenderContext)
        val jpaQuery = entityManager.createQuery(rendered.query, ChatMessage::class.java).apply {
            rendered.params.forEach { (name, value) ->
                setParameter(name, value)
            }
        }
        return jpaQuery.setMaxResults(size).resultList
    }
}
