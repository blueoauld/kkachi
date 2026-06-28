package com.blueoauld.server.member.presentation

import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.member.application.MemberImageService
import com.blueoauld.server.member.application.request.CreateImageUploadUrlRequest
import com.blueoauld.server.member.application.request.UpdateImagesRequest
import com.blueoauld.server.member.application.response.ImageUploadUrlResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/v1/members/me/images")
    fun updateImages(
        @LoginMember memberId: Long,
        @Valid @RequestBody request: UpdateImagesRequest,
    ): ResponseEntity<Unit> {
        memberImageService.updateImages(memberId, request)
        return ResponseEntity.ok().build()
    }
}
