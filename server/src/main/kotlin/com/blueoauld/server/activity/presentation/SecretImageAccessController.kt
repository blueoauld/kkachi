package com.blueoauld.server.activity.presentation

import com.blueoauld.server.activity.application.SecretImageAccessService
import com.blueoauld.server.activity.application.response.SecretImageAccessResponse
import com.blueoauld.server.global.resolver.LoginMember
import com.blueoauld.server.global.response.CursorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class SecretImageAccessController(

    private val secretImageAccessService: SecretImageAccessService,
) {

    @PostMapping("/v1/secret-images/viewers/{viewerId}")
    fun openSecretImage(
        @LoginMember memberId: Long,
        @PathVariable viewerId: Long,
    ): ResponseEntity<Unit> {
        secretImageAccessService.openSecretImage(memberId, viewerId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/v1/secret-images/viewers/{viewerId}")
    fun closeSecretImage(
        @LoginMember memberId: Long,
        @PathVariable viewerId: Long,
    ): ResponseEntity<Unit> {
        secretImageAccessService.closeSecretImage(memberId, viewerId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/v1/secret-images/viewers")
    fun getSecretImageViewers(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<SecretImageAccessResponse>> {
        val response = secretImageAccessService.getSecretImageViewers(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/v1/secret-images/owners")
    fun getSecretImageOwners(
        @LoginMember memberId: Long,
        @RequestParam(required = false) cursor: Long?,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CursorResponse<SecretImageAccessResponse>> {
        val response = secretImageAccessService.getSecretImageOwners(memberId, cursor, size)
        return ResponseEntity.ok(response)
    }
}
