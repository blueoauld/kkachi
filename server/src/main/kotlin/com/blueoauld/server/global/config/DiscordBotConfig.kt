package com.blueoauld.server.global.config

import com.blueoauld.server.global.discord.AdminSlashCommandHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(prefix = "discord.bot", name = ["enabled"], havingValue = "true")
@Configuration
class DiscordBotConfig(

    @Value($$"${discord.bot.token}") private val token: String,
    @Value($$"${discord.bot.guild-id}") private val guildId: String,

    private val handlers: List<AdminSlashCommandHandler>,
) {

    @Bean(destroyMethod = "shutdown")
    fun jda(): JDA {
        val builder = JDABuilder.createLight(token)
        handlers.forEach { builder.addEventListeners(it) }
        val jda = builder.build().awaitReady()

        val commands = handlers.flatMap { it.commands() }
        if (guildId.isNotBlank()) {
            jda.getGuildById(guildId)?.updateCommands()?.addCommands(commands)?.queue()
        } else {
            jda.updateCommands().addCommands(commands).queue()
        }

        return jda
    }
}