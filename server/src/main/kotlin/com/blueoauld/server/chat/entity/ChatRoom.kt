package com.blueoauld.server.chat.entity

import com.blueoauld.server.chat.entity.type.MessageType
import jakarta.persistence.*
import org.hibernate.annotations.SoftDelete
import org.hibernate.annotations.SoftDeleteType
import java.time.Instant

@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
@Entity
@Table(name = "chat_room")
class ChatRoom(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "member1_id", nullable = false)
    val member1Id: Long,

    @Column(name = "member2_id", nullable = false)
    val member2Id: Long,

    @Column(name = "member1_last_read_message_id", nullable = false)
    var member1LastReadMessageId: Long = 0,

    @Column(name = "member2_last_read_message_id", nullable = false)
    var member2LastReadMessageId: Long = 0,

    @Column(name = "last_message_id")
    var lastMessageId: Long? = null,

    @Column(name = "last_message_content", length = 200)
    var lastMessageContent: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "last_message_type")
    var lastMessageType: MessageType? = null,

    @Column(name = "last_message_at")
    var lastMessageAt: Instant? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    companion object {
        fun of(memberId: Long, opponentId: Long): ChatRoom {
            val (member1Id, member2Id) = if (memberId < opponentId) memberId to opponentId else opponentId to memberId
            return ChatRoom(member1Id = member1Id, member2Id = member2Id)
        }
    }

    fun isMember(memberId: Long): Boolean = memberId == member1Id || memberId == member2Id

    fun opponentIdOf(memberId: Long): Long = if (memberId == member1Id) member2Id else member1Id

    fun lastReadMessageIdOf(memberId: Long): Long =
        if (memberId == member1Id) member1LastReadMessageId else member2LastReadMessageId

    fun updateLastMessage(messageId: Long, preview: String, type: MessageType, at: Instant) {
        this.lastMessageId = messageId
        this.lastMessageContent = preview
        this.lastMessageType = type
        this.lastMessageAt = at
    }

    fun markRead(memberId: Long, messageId: Long) {
        if (memberId == member1Id) {
            if (messageId > member1LastReadMessageId) member1LastReadMessageId = messageId
        } else {
            if (messageId > member2LastReadMessageId) member2LastReadMessageId = messageId
        }
    }

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
