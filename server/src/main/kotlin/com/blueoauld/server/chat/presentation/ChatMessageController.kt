package com.blueoauld.server.chat.presentation

import com.blueoauld.server.chat.application.ChatMessageService
import com.blueoauld.server.chat.application.request.CreateChatImageUploadUrlRequest
import com.blueoauld.server.chat.application.request.SendMessageRequest
import com.blueoauld.server.chat.application.response.ChatImageUploadUrlResponse
import com.blueoauld.server.chat.application.response.ChatMessageResponse
import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class ChatMessageController(

    private val chatMessageService: ChatMessageService,
) {

    @PostMapping("/v1/chat/images/presigned-url")
    fun createImageUploadUrl(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: CreateChatImageUploadUrlRequest,
    ): ResponseEntity<ChatImageUploadUrlResponse> {
        val response = chatMessageService.createImageUploadUrl(memberId, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/v1/chat/rooms/{roomId}/messages")
    fun sendMessage(
        @LoginMember memberId: Long,
        @PathVariable roomId: Long,
        @Valid @RequestBody request: SendMessageRequest,
    ): ResponseEntity<ChatMessageResponse> {
        val response = chatMessageService.sendMessage(memberId, roomId, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/v1/chat/rooms/{roomId}/messages")
    fun getMessages(
        @LoginMember memberId: Long,
        @PathVariable roomId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "30") size: Int,
    ): ResponseEntity<CursorResponse<ChatMessageResponse>> {
        val response = chatMessageService.getMessages(memberId, roomId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
