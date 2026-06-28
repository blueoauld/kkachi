package com.blueoauld.server.global.security

fun interface CodeGenerator {

    fun numeric(length: Int): String
}
