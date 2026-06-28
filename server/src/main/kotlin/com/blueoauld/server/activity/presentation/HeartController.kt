package com.blueoauld.server.activity.presentation

import com.blueoauld.server.activity.application.HeartService
import com.blueoauld.server.global.resolver.LoginMember
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
}
