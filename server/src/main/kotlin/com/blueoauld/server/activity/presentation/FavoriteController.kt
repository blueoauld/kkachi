package com.blueoauld.server.activity.presentation

import com.blueoauld.server.activity.application.FavoriteService
import com.blueoauld.server.activity.application.response.SentFavoriteResponse
import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class FavoriteController(

    private val favoriteService: FavoriteService,
) {

    @PostMapping("/v1/favorites/{targetId}")
    fun addFavorite(
        @LoginMember memberId: Long,
        @PathVariable targetId: Long,
    ): ResponseEntity<Unit> {
        favoriteService.addFavorite(memberId, targetId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/v1/favorites/{targetId}")
    fun removeFavorite(
        @LoginMember memberId: Long,
        @PathVariable targetId: Long,
    ): ResponseEntity<Unit> {
        favoriteService.removeFavorite(memberId, targetId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/favorites/sent")
    fun getSentFavorites(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<SentFavoriteResponse>> {
        val response = favoriteService.getSentFavorites(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
