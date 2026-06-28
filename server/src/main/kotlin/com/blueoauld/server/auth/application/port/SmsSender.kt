package com.blueoauld.server.auth.application.port

fun interface SmsSender {

    fun send(phone: String, message: String)
}