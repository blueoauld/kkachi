package com.blueoauld.server.auth.presentation

import com.blueoauld.server.auth.application.AuthService
import com.blueoauld.server.auth.application.request.SendVerificationCodeRequest
import com.blueoauld.server.auth.application.request.SignupRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class AuthController(

    private val authService: AuthService,
) {

    @PostMapping("/v1/auth/verification-code")
    fun sendVerificationCode(@Valid @RequestBody request: SendVerificationCodeRequest): ResponseEntity<Unit> {
        authService.sendVerificationCode(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/v1/auth/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): ResponseEntity<Unit> {
        authService.signup(request)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}