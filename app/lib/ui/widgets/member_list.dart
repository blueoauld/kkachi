import 'package:app/model/member.dart';
import 'package:app/ui/widgets/member_list_tile.dart';
import 'package:flutter/cupertino.dart';

class MemberList extends StatelessWidget {
  const MemberList({
    super.key,
    required this.members,
    this.onRefresh,
    this.onTapMember,
    this.ranked = false,
  });

  final List<Member> members;

  /// 당겨서 새로고침 콜백. null이면 새로고침을 비활성화한다.
  final Future<void> Function()? onRefresh;
  final void Function(Member member)? onTapMember;

  /// true이면 순위(1부터)를 표시한다. 상위 1~3등은 메달이 붙는다.
  final bool ranked;

  @override
  Widget build(BuildContext context) {
    return CupertinoScrollbar(
      child: CustomScrollView(
        slivers: [
          if (onRefresh != null)
            CupertinoSliverRefreshControl(onRefresh: onRefresh),
          SliverPadding(
            // 하단 탭바(높이 60 + 안전영역) 뒤로 콘텐츠가 스크롤되도록 여백 확보.
            padding: EdgeInsets.only(
              bottom: 12 + 60 + MediaQuery.of(context).padding.bottom,
            ),
            sliver: SliverList.builder(
              itemCount: members.length,
              itemBuilder: (context, index) {
                final member = members[index];
                return MemberListTile(
                  member: member,
                  rank: ranked ? index + 1 : null,
                  onTap: () => onTapMember?.call(member),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
