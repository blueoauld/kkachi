package com.blueoauld.server.suspension.infrastructure

import com.blueoauld.server.global.exception.BusinessException
import com.blueoauld.server.suspension.application.SuspensionService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SuspensionCommandListener(

    @Value($$"${discord.bot.admin-role-id}") private val adminRoleId: String,

    private val suspensionService: SuspensionService,
) : ListenerAdapter() {

    companion object {
        const val COMMAND_NAME = "정지"
        const val OPTION_PHONE = "휴대폰번호"
        const val OPTION_REASON = "사유"
        const val OPTION_DAYS = "일수"
    }

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != COMMAND_NAME) {
            return
        }

        val member = event.member
        if (member == null || member.roles.none { it.id == adminRoleId }) {
            event.reply("이 명령어를 사용할 권한이 없습니다.").setEphemeral(true).queue()
            return
        }

        val phone = event.getOption(OPTION_PHONE)!!.asString
        val reason = event.getOption(OPTION_REASON)!!.asString
        val days = event.getOption(OPTION_DAYS)!!.asLong

        event.deferReply(true).queue()

        try {
            suspensionService.suspend(phone, reason, days)
            event.hook.sendMessage("$phone 회원을 ${days}일간 정지했습니다. (사유: $reason)").queue()
        } catch (e: BusinessException) {
            event.hook.sendMessage("정지 실패: ${e.errorCode.message}").queue()
        } catch (e: Exception) {
            log.error("정지 처리 중 오류가 발생했습니다. phone = $phone", e)
            event.hook.sendMessage("정지 처리 중 오류가 발생했습니다.").queue()
        }
    }
}
