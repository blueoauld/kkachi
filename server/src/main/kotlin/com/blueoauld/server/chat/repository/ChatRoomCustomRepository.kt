package com.blueoauld.server.chat.repository

import com.blueoauld.server.chat.repository.result.ChatRoomResult

interface ChatRoomCustomRepository {

    fun findChatRooms(memberId: Long, cursorId: Long?, size: Int): List<ChatRoomResult>
}
