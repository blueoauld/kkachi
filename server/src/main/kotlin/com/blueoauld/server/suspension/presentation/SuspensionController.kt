package com.blueoauld.server.suspension.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.suspension.application.SuspensionService
import com.blueoauld.server.suspension.application.response.SuspensionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class SuspensionController(

    private val suspensionService: SuspensionService,
) {

    @GetMapping("/v1/suspensions/me")
    fun getMySuspension(@LoginMember memberId: Long): ResponseEntity<SuspensionResponse> {
        return ResponseEntity.ok(suspensionService.getMySuspension(memberId))
    }
}
