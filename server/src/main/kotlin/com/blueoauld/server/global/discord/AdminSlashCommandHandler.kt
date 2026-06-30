package com.blueoauld.server.global.discord

import com.blueoauld.server.global.exception.BusinessException
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.slf4j.LoggerFactory

abstract class AdminSlashCommandHandler(

    private val adminRoleId: String,
) : ListenerAdapter() {

    protected val log = LoggerFactory.getLogger(javaClass)

    private val commandNames: Set<String> by lazy { commands().map { it.name }.toSet() }

    abstract fun commands(): List<SlashCommandData>

    protected abstract fun handle(event: SlashCommandInteractionEvent)

    final override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name !in commandNames) {
            return
        }

        val member = event.member
        if (member == null || member.roles.none { it.id == adminRoleId }) {
            event.reply("이 명령어를 사용할 권한이 없습니다.").setEphemeral(true).queue()
            return
        }

        event.deferReply(true).queue()

        try {
            handle(event)
        } catch (e: BusinessException) {
            event.hook.sendMessage("처리 실패: ${e.errorCode.message}").queue()
        } catch (e: Exception) {
            log.error("명령어 처리 중 오류가 발생했습니다. command = ${event.name}", e)
            event.hook.sendMessage("처리 중 오류가 발생했습니다.").queue()
        }
    }
}
