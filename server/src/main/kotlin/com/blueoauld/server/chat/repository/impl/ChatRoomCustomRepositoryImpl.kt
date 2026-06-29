package com.blueoauld.server.chat.repository.impl

import com.blueoauld.server.chat.entity.ChatRoom
import com.blueoauld.server.chat.repository.ChatRoomCustomRepository
import com.blueoauld.server.chat.repository.result.ChatRoomResult
import com.blueoauld.server.member.entity.Member
import com.blueoauld.server.member.entity.MemberImage
import com.blueoauld.server.member.entity.type.ImageType
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class ChatRoomCustomRepositoryImpl(

    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : ChatRoomCustomRepository {

    private val jpqlRenderer = JpqlRenderer()

    override fun findChatRooms(memberId: Long, cursorId: Long?, size: Int): List<ChatRoomResult> {
        val query = jpql {
            selectNew<ChatRoomResult>(
                path(ChatRoom::id),
                path(ChatRoom::member1Id),
                path(ChatRoom::member1LastReadMessageId),
                path(ChatRoom::member2LastReadMessageId),
                path(Member::id),
                path(MemberImage::objectKey),
                path(Member::nickname),
                path(ChatRoom::lastMessageId),
                path(ChatRoom::lastMessageContent),
                path(ChatRoom::lastMessageType),
                path(ChatRoom::lastMessageAt),
            ).from(
                entity(ChatRoom::class),
                join(Member::class).on(
                    path(Member::id).eq(path(ChatRoom::member2Id))
                        .and(path(ChatRoom::member1Id).eq(memberId))
                        .or(
                            path(Member::id).eq(path(ChatRoom::member1Id))
                                .and(path(ChatRoom::member2Id).eq(memberId)),
                        ),
                ),
                leftJoin(MemberImage::class).on(
                    path(MemberImage::memberId)
                        .eq(path(Member::id))
                        .and(path(MemberImage::type).eq(ImageType.PUBLIC))
                        .and(path(MemberImage::displayOrder).eq(0)),
                ),
            ).whereAnd(
                path(ChatRoom::member1Id).eq(memberId).or(path(ChatRoom::member2Id).eq(memberId)),
                path(ChatRoom::lastMessageId).isNotNull(),
                cursorId?.let {
                    path(ChatRoom::lastMessageId).lt(it)
                },
            ).orderBy(
                path(ChatRoom::lastMessageId).desc(),
            )
        }

        val rendered = jpqlRenderer.render(query, jpqlRenderContext)
        val jpaQuery = entityManager.createQuery(rendered.query, ChatRoomResult::class.java).apply {
            rendered.params.forEach { (name, value) ->
                setParameter(name, value)
            }
        }
        return jpaQuery.setMaxResults(size).resultList
    }
}
