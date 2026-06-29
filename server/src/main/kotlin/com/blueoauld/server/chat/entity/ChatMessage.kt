package com.blueoauld.server.chat.entity

import com.blueoauld.server.chat.entity.type.MessageType
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "chat_message",
    indexes = [
        Index(name = "idx_chat_message_room_id", columnList = "room_id, id"),
    ],
)
class ChatMessage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "room_id", nullable = false)
    val roomId: Long,

    @Column(name = "sender_id", nullable = false)
    val senderId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: MessageType,

    @Column(name = "content", length = 1000, nullable = false)
    var content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),
) {

    @PrePersist
    fun onCreate() {
        this.createdAt = Instant.now()
    }
}
