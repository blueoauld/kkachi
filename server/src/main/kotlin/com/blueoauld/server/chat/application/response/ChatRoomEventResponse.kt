package com.blueoauld.server.chat.application.response

data class ChatRoomEventResponse(

    val type: ChatRoomEventType,
    val roomId: Long,
) {

    enum class ChatRoomEventType {
        ROOM_DELETED,
    }

    companion object {
        fun roomDeleted(roomId: Long): ChatRoomEventResponse =
            ChatRoomEventResponse(ChatRoomEventType.ROOM_DELETED, roomId)
    }
}
