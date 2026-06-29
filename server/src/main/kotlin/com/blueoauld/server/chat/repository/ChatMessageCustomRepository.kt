package com.blueoauld.server.chat.repository

import com.blueoauld.server.chat.entity.ChatMessage

interface ChatMessageCustomRepository {

    fun findMessages(roomId: Long, cursorId: Long?, size: Int): List<ChatMessage>
}
