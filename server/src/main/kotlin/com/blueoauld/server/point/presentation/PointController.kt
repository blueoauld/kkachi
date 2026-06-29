package com.blueoauld.server.point.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import com.blueoauld.server.point.application.PointService
import com.blueoauld.server.point.application.response.PointBalanceResponse
import com.blueoauld.server.point.application.response.PointHistoryResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class PointController(

    private val pointService: PointService,
) {

    @GetMapping("/v1/points")
    fun getBalance(
        @LoginMember memberId: Long,
    ): ResponseEntity<PointBalanceResponse> {
        val response = pointService.getBalance(memberId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/v1/points/histories")
    fun getHistories(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<PointHistoryResponse>> {
        val response = pointService.getHistories(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
