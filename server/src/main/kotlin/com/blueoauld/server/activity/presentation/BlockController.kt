package com.blueoauld.server.activity.presentation

import com.blueoauld.server.activity.application.BlockService
import com.blueoauld.server.global.resolver.LoginMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class BlockController(

    private val blockService: BlockService,
) {

    @PostMapping("/v1/blocks/{blockedId}")
    fun block(
        @LoginMember memberId: Long,
        @PathVariable blockedId: Long,
    ): ResponseEntity<Unit> {
        blockService.block(memberId, blockedId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/v1/blocks/{blockedId}")
    fun unblock(
        @LoginMember memberId: Long,
        @PathVariable blockedId: Long,
    ): ResponseEntity<Unit> {
        blockService.unblock(memberId, blockedId)
        return ResponseEntity.noContent().build()
    }
}
