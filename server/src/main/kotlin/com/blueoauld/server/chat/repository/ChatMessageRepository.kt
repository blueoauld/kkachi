package com.blueoauld.server.chat.repository

import com.blueoauld.server.chat.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long>, ChatMessageCustomRepository {

    fun countByRoomIdAndSenderIdAndIdGreaterThan(roomId: Long, senderId: Long, id: Long): Long
}
