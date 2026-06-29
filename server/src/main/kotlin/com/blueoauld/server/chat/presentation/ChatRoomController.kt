package com.blueoauld.server.chat.presentation

import com.blueoauld.server.chat.application.ChatRoomService
import com.blueoauld.server.chat.application.request.CreateChatRoomRequest
import com.blueoauld.server.chat.application.response.ChatRoomResponse
import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class ChatRoomController(

    private val chatRoomService: ChatRoomService,
) {

    @PostMapping("/v1/chat/rooms")
    fun createOrGetRoom(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: CreateChatRoomRequest,
    ): ResponseEntity<ChatRoomResponse> {
        val response = chatRoomService.createOrGetRoom(memberId, request.opponentId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/v1/chat/rooms")
    fun getChatRooms(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<ChatRoomResponse>> {
        val response = chatRoomService.getChatRooms(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/v1/chat/rooms/{roomId}")
    fun deleteRoom(
        @LoginMember memberId: Long,
        @PathVariable roomId: Long,
    ): ResponseEntity<Unit> {
        chatRoomService.deleteRoom(memberId, roomId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/v1/chat/rooms/{roomId}/read")
    fun markAsRead(
        @LoginMember memberId: Long,
        @PathVariable roomId: Long,
    ): ResponseEntity<Unit> {
        chatRoomService.markAsRead(memberId, roomId)
        return ResponseEntity.ok().build()
    }
}
