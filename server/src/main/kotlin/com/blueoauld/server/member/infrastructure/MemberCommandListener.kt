package com.blueoauld.server.member.infrastructure

import com.blueoauld.server.global.discord.AdminSlashCommandHandler
import com.blueoauld.server.member.application.MemberResetTarget
import com.blueoauld.server.member.application.MemberService
import com.blueoauld.server.member.application.response.MemberDetailResponse
import com.blueoauld.server.member.entity.type.GenderType
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class MemberCommandListener(

    @Value($$"${discord.bot.admin-role-id}") adminRoleId: String,

    private val memberService: MemberService,
) : AdminSlashCommandHandler(adminRoleId) {

    companion object {
        const val COMMAND_LOOKUP = "조회"
        const val COMMAND_RESET = "초기화"

        const val OPTION_NICKNAME = "닉네임"
        const val OPTION_MEMBER_ID = "회원아이디"
        const val OPTION_TARGET = "항목"

        private val KST_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"))
    }

    override fun commands(): List<SlashCommandData> = listOf(
        Commands.slash(COMMAND_LOOKUP, "닉네임으로 회원을 조회합니다.")
            .addOption(OptionType.STRING, OPTION_NICKNAME, "조회할 회원의 닉네임", true),
        Commands.slash(COMMAND_RESET, "회원의 특정 항목을 초기화/제재합니다.")
            .addOption(OptionType.INTEGER, OPTION_MEMBER_ID, "초기화할 회원 ID", true)
            .addOptions(targetOption()),
    )

    override fun handle(event: SlashCommandInteractionEvent) {
        when (event.name) {
            COMMAND_LOOKUP -> handleLookup(event)
            COMMAND_RESET -> handleReset(event)
        }
    }

    private fun handleLookup(event: SlashCommandInteractionEvent) {
        val nickname = event.getOption(OPTION_NICKNAME)!!.asString
        val member = memberService.getMemberDetailByNickname(nickname)

        event.hook.sendMessageEmbeds(
            infoEmbed(member),
            imageEmbed("공개 사진", member.publicImageUrls),
            imageEmbed("비밀 사진", member.secretImageUrls),
        ).queue()
    }

    private fun handleReset(event: SlashCommandInteractionEvent) {
        val memberId = event.getOption(OPTION_MEMBER_ID)!!.asLong
        val target = MemberResetTarget.valueOf(event.getOption(OPTION_TARGET)!!.asString)

        memberService.reset(memberId, target)

        event.hook.sendMessage("회원(ID: `$memberId`)의 ${target.label} 항목을 초기화했습니다.").queue()
    }

    private fun targetOption() = OptionData(OptionType.STRING, OPTION_TARGET, "초기화할 항목", true).apply {
        MemberResetTarget.entries.forEach { addChoice(it.label, it.name) }
    }

    private fun infoEmbed(member: MemberDetailResponse) = EmbedBuilder()
        .setTitle("회원 정보")
        .addField("ID", "`${member.id}`", false)
        .addField("닉네임", "`${member.nickname}`", false)
        .addField("휴대폰", "`${member.phone}`", false)
        .addField("성별", genderText(member.gender), true)
        .addField("출생연도", member.birthYear.toString(), true)
        .addField("코멘트", member.comment, false)
        .addField("자기소개", member.bio, false)
        .addField("가입일", formatKst(member.createdAt), false)
        .addField("갱신일", formatKst(member.updatedAt), false)
        .build()

    private fun imageEmbed(title: String, urls: List<String>) = EmbedBuilder()
        .setTitle(title)
        .setDescription(
            if (urls.isEmpty()) {
                "없음"
            } else {
                urls.mapIndexed { index, url -> "${index + 1}. $url" }.joinToString("\n")
            },
        )
        .build()

    private fun formatKst(instant: Instant): String = KST_FORMATTER.format(instant)

    private fun genderText(gender: GenderType): String = when (gender) {
        GenderType.MALE -> "남자"
        GenderType.FEMALE -> "여자"
    }
}
