import 'package:app/model/member.dart';
import 'package:app/ui/widgets/member_list_tile.dart';
import 'package:flutter/cupertino.dart';

class MemberList extends StatelessWidget {
  const MemberList({
    super.key,
    required this.members,
    required this.onRefresh,
    this.onTapMember,
  });

  final List<Member> members;
  final Future<void> Function() onRefresh;
  final void Function(Member member)? onTapMember;

  @override
  Widget build(BuildContext context) {
    return CupertinoScrollbar(
      child: CustomScrollView(
        slivers: [
          CupertinoSliverRefreshControl(onRefresh: onRefresh),
          SliverPadding(
            padding: const EdgeInsets.only(bottom: 12),
            sliver: SliverList.builder(
              itemCount: members.length,
              itemBuilder: (context, index) {
                final member = members[index];
                return MemberListTile(
                  member: member,
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
