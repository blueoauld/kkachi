package com.blueoauld.server.suspension.infrastructure

import com.blueoauld.server.global.discord.AdminSlashCommandHandler
import com.blueoauld.server.suspension.application.SuspensionService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SuspensionCommandListener(

    @Value($$"${discord.bot.admin-role-id}") adminRoleId: String,

    private val suspensionService: SuspensionService,
) : AdminSlashCommandHandler(adminRoleId) {

    companion object {
        const val COMMAND_SUSPEND = "정지"
        const val COMMAND_LOOKUP = "정지조회"
        const val COMMAND_RELEASE = "정지해제"

        const val OPTION_PHONE = "휴대폰번호"
        const val OPTION_REASON = "사유"
        const val OPTION_DAYS = "일수"
    }

    override fun commands(): List<SlashCommandData> = listOf(
        Commands.slash(COMMAND_SUSPEND, "회원을 정지합니다.")
            .addOption(OptionType.STRING, OPTION_PHONE, "정지할 회원의 휴대폰 번호", true)
            .addOption(OptionType.STRING, OPTION_REASON, "정지 사유", true)
            .addOption(OptionType.INTEGER, OPTION_DAYS, "정지 일수", true),
        Commands.slash(COMMAND_LOOKUP, "회원의 정지 상태를 조회합니다.")
            .addOption(OptionType.STRING, OPTION_PHONE, "조회할 회원의 휴대폰 번호", true),
        Commands.slash(COMMAND_RELEASE, "회원의 정지를 해제합니다.")
            .addOption(OptionType.STRING, OPTION_PHONE, "해제할 회원의 휴대폰 번호", true),
    )

    override fun handle(event: SlashCommandInteractionEvent) {
        val phone = event.getOption(OPTION_PHONE)!!.asString

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
    }
}
