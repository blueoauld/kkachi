package com.blueoauld.server.suspension.application.response

data class SuspensionResponse(

    val reason: String,
    val remainingDays: Long,
)
