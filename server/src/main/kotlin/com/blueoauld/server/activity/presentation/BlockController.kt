package com.blueoauld.server.activity.presentation

import com.blueoauld.server.activity.application.BlockService
import com.blueoauld.server.activity.application.response.BlockResponse
import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/v1/blocks")
    fun getBlocks(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<BlockResponse>> {
        val response = blockService.getBlocks(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
