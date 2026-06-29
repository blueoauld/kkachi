package com.blueoauld.server.chat.application

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Component
class ChatEventPublisher(

    private val messagingTemplate: SimpMessagingTemplate,
) {

    fun sendToUser(userId: Long, destination: String, payload: Any) {
        afterCommit {
            messagingTemplate.convertAndSendToUser(userId.toString(), destination, payload)
        }
    }

    private fun afterCommit(action: () -> Unit) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                object : TransactionSynchronization {
                    override fun afterCommit() = action()
                },
            )
        } else {
            action()
        }
    }
}
