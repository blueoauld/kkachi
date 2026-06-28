package com.blueoauld.server.auth.application.response

data class LoginResponse(

    val accessToken: String,
    val refreshToken: String,
)
