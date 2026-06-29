package com.blueoauld.server.point.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.point.application.AdRewardService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class AdRewardController(

    private val adRewardService: AdRewardService,
) {

    @PostMapping("/v1/ad-rewards")
    fun reward(
        @LoginMember memberId: Long,
    ): ResponseEntity<Unit> {
        adRewardService.reward(memberId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
