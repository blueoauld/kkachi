package com.blueoauld.server.activity.presentation

import com.blueoauld.server.activity.application.HeartService
import com.blueoauld.server.activity.application.response.HeartResponse
import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class HeartController(

    private val heartService: HeartService,
) {

    @PostMapping("/v1/hearts/{receiverId}")
    fun sendHeart(
        @LoginMember memberId: Long,
        @PathVariable receiverId: Long,
    ): ResponseEntity<Unit> {
        heartService.sendHeart(memberId, receiverId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/v1/hearts/{receiverId}")
    fun cancelHeart(
        @LoginMember memberId: Long,
        @PathVariable receiverId: Long,
    ): ResponseEntity<Unit> {
        heartService.cancelHeart(memberId, receiverId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/hearts/sent")
    fun getSentHearts(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<HeartResponse>> {
        val response = heartService.getSentHearts(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/v1/hearts/received")
    fun getReceivedHearts(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<HeartResponse>> {
        val response = heartService.getReceivedHearts(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
