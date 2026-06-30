package com.blueoauld.server.member.application

enum class MemberResetTarget(val label: String) {

    NICKNAME("닉네임"),
    COMMENT("코멘트"),
    BIO("자기소개"),
    PUBLIC_IMAGE("공개사진"),
    SECRET_IMAGE("비밀사진"),
}
