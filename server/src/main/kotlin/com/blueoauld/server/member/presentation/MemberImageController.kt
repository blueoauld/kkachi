package com.blueoauld.server.member.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.member.application.MemberImageService
import com.blueoauld.server.member.application.request.CreateImageUploadUrlRequest
import com.blueoauld.server.member.application.response.ImageUploadUrlResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class MemberImageController(

    private val memberImageService: MemberImageService,
) {

    @PostMapping("/v1/members/me/images/presigned-url")
    fun createUploadUrl(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: CreateImageUploadUrlRequest,
    ): ResponseEntity<ImageUploadUrlResponse> {
        val response = memberImageService.createUploadUrl(memberId, request)
        return ResponseEntity.ok(response)
    }
}
