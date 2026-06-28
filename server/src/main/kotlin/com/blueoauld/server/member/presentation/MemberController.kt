package com.blueoauld.server.member.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.member.application.MemberService
import com.blueoauld.server.member.application.request.UpdateCommentRequest
import com.blueoauld.server.member.application.request.UpdateLocationRequest
import com.blueoauld.server.member.application.request.UpdateProfileRequest
import com.blueoauld.server.member.application.response.MemberProfileResponse
import com.blueoauld.server.member.application.response.MyProfileResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class MemberController(

    private val memberService: MemberService,
) {

    @GetMapping("/v1/members/me")
    fun getMyProfile(@LoginMember memberId: Long): ResponseEntity<MyProfileResponse> {
        return ResponseEntity.ok(memberService.getMyProfile(memberId))
    }

    @GetMapping("/v1/members/{targetId}")
    fun getMemberProfile(
        @LoginMember memberId: Long,
        @PathVariable targetId: Long,
    ): ResponseEntity<MemberProfileResponse> {
        val response = memberService.getMemberProfile(memberId, targetId)
        return ResponseEntity.ok(response)
    }

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

    @PostMapping("/v1/members/me/bump")
    fun bump(@LoginMember memberId: Long): ResponseEntity<Unit> {
        memberService.bump(memberId)
        return ResponseEntity.ok().build()
    }
}
