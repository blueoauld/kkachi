import 'package:app/model/member.dart';
import 'package:app/ui/widgets/member_list_tile.dart';
import 'package:flutter/cupertino.dart';

/// 회원 검색 결과 리스트.
///
/// 탭바를 피하기 위한 하단 여백이 있는 [MemberList]와 달리,
/// 풀스크린 검색 화면에 맞춰 불필요한 하단 여백 없이 표시한다.
class MemberSearchList extends StatelessWidget {
  const MemberSearchList({super.key, required this.members, this.onTapMember});

  final List<Member> members;
  final void Function(Member member)? onTapMember;

  @override
  Widget build(BuildContext context) {
    return CupertinoScrollbar(
      child: ListView.builder(
        // 키보드가 올라와도 결과를 스크롤할 수 있도록 한다.
        keyboardDismissBehavior: ScrollViewKeyboardDismissBehavior.onDrag,
        padding: EdgeInsets.only(
          bottom: 12 + MediaQuery.of(context).padding.bottom,
        ),
        itemCount: members.length,
        itemBuilder: (context, index) {
          final member = members[index];
          return MemberListTile(
            member: member,
            onTap: () => onTapMember?.call(member),
          );
        },
      ),
    );
  }
}
