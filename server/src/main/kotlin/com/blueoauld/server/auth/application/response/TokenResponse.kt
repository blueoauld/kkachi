package com.blueoauld.server.auth.application.response

data class TokenResponse(

    val accessToken: String,
    val refreshToken: String,
)
