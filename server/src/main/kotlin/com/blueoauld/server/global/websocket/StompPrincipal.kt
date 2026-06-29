package com.blueoauld.server.global.websocket

import java.security.Principal

class StompPrincipal(

    private val memberId: Long,
) : Principal {

    override fun getName(): String = memberId.toString()
}
