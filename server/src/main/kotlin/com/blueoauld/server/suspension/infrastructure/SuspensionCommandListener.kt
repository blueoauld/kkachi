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
        const val COMMAND_SUSPEND = "정지"
        const val COMMAND_LOOKUP = "정지조회"
        const val COMMAND_RELEASE = "정지해제"

        const val OPTION_PHONE = "휴대폰번호"
        const val OPTION_REASON = "사유"
        const val OPTION_DAYS = "일수"

        private val COMMANDS = setOf(COMMAND_SUSPEND, COMMAND_LOOKUP, COMMAND_RELEASE)
    }

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name !in COMMANDS) {
            return
        }

        val member = event.member
        if (member == null || member.roles.none { it.id == adminRoleId }) {
            event.reply("이 명령어를 사용할 권한이 없습니다.").setEphemeral(true).queue()
            return
        }

        event.deferReply(true).queue()
        val phone = event.getOption(OPTION_PHONE)!!.asString

        try {
            val message = when (event.name) {
                COMMAND_SUSPEND -> {
                    val reason = event.getOption(OPTION_REASON)!!.asString
                    val days = event.getOption(OPTION_DAYS)!!.asLong
                    suspensionService.suspend(phone, reason, days)
                    "`$phone` 회원을 `${days}`일간 정지했습니다. (사유: `$reason`)"
                }

                COMMAND_LOOKUP -> {
                    val count = suspensionService.countSuspensions(phone)
                    val suspension = suspensionService.lookup(phone)

                    if (suspension == null) {
                        "`$phone` 회원은 정지 상태가 아닙니다. (누적 정지 횟수: `${count}`회)"
                    } else {
                        "`$phone` 회원은 정지 상태입니다. (사유: `${suspension.reason}`, 남은 일수: `${suspension.remainingDays}`일, 누적 정지 횟수: `${count}`회)"
                    }
                }

                COMMAND_RELEASE -> {
                    val released = suspensionService.release(phone)
                    if (released == 0L) {
                        "`$phone` 회원은 정지 상태가 아닙니다."
                    } else {
                        "`$phone` 회원의 정지를 해제했습니다."
                    }
                }

                else -> return
            }
            event.hook.sendMessage(message).queue()
        } catch (e: BusinessException) {
            event.hook.sendMessage("처리 실패: ${e.errorCode.message}").queue()
        } catch (e: Exception) {
            log.error("명령어 처리 중 오류가 발생했습니다. command = ${event.name}, phone = $phone", e)
            event.hook.sendMessage("처리 중 오류가 발생했습니다.").queue()
        }
    }
}
