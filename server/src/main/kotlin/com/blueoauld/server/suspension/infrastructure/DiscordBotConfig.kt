package com.blueoauld.server.suspension.infrastructure

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "discord.bot", name = ["enabled"], havingValue = "true")
class DiscordBotConfig(

    @Value("\${discord.bot.token}") private val token: String,
    @Value("\${discord.bot.guild-id}") private val guildId: String,

    private val suspensionCommandListener: SuspensionCommandListener,
) {

    @Bean(destroyMethod = "shutdown")
    fun jda(): JDA {
        val jda = JDABuilder.createLight(token)
            .addEventListeners(suspensionCommandListener)
            .build()
            .awaitReady()

        val command = Commands.slash(SuspensionCommandListener.COMMAND_NAME, "회원을 정지합니다.")
            .addOption(OptionType.STRING, SuspensionCommandListener.OPTION_PHONE, "정지할 회원의 휴대폰 번호", true)
            .addOption(OptionType.STRING, SuspensionCommandListener.OPTION_REASON, "정지 사유", true)
            .addOption(OptionType.INTEGER, SuspensionCommandListener.OPTION_DAYS, "정지 일수", true)

        if (guildId.isNotBlank()) {
            jda.getGuildById(guildId)?.upsertCommand(command)?.queue()
        } else {
            jda.upsertCommand(command).queue()
        }
        return jda
    }
}
