package com.blueoauld.server.member.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.member.application.MemberService
import com.blueoauld.server.member.application.request.UpdateCommentRequest
import com.blueoauld.server.member.application.request.UpdateLocationRequest
import com.blueoauld.server.member.application.request.UpdateProfileRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class MemberController(

    private val memberService: MemberService,
) {

    @PatchMapping("/v1/members/me/profile")
    fun updateProfile(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: UpdateProfileRequest,
    ): ResponseEntity<Unit> {
        memberService.updateProfile(memberId, request)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/v1/members/me/location")
    fun updateLocation(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: UpdateLocationRequest,
    ): ResponseEntity<Unit> {
        memberService.updateLocation(memberId, request)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/v1/members/me/comment")
    fun updateComment(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: UpdateCommentRequest,
    ): ResponseEntity<Unit> {
        memberService.updateComment(memberId, request)
        return ResponseEntity.ok().build()
    }
}
