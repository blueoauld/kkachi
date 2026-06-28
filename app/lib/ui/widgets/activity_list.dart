import 'package:app/model/member_activity.dart';
import 'package:app/ui/widgets/activity_list_tile.dart';
import 'package:flutter/cupertino.dart';

/// 활동 목록(좋아요/비밀 사진/차단 등) 리스트.
///
/// [MemberList]를 참고하되, 풀스크린으로 푸시되는 화면에 맞춰
/// 탭바용 하단 여백 없이 표시하고 [ActivityListTile]로 행을 그린다.
/// 목록이 비어 있으면 [emptyMessage]를 가운데에 보여준다.
class ActivityList extends StatelessWidget {
  const ActivityList({
    super.key,
    required this.members,
    this.onTapMember,
    this.onDeleteMember,
    this.emptyMessage = '목록이 비어 있습니다.',
  });

  final List<MemberActivity> members;
  final void Function(MemberActivity member)? onTapMember;

  /// 행 오른쪽 휴지통 버튼 콜백. (목록에서 삭제)
  final void Function(MemberActivity member)? onDeleteMember;
  final String emptyMessage;

  @override
  Widget build(BuildContext context) {
    if (members.isEmpty) {
      return Center(
        child: Text(
          emptyMessage,
          style: TextStyle(
            color: CupertinoColors.secondaryLabel.resolveFrom(context),
          ),
        ),
      );
    }

    return CupertinoScrollbar(
      child: ListView.builder(
        padding: EdgeInsets.only(
          bottom: 12 + MediaQuery.of(context).padding.bottom,
        ),
        itemCount: members.length,
        itemBuilder: (context, index) {
          final member = members[index];
          return ActivityListTile(
            member: member,
            onTap: () => onTapMember?.call(member),
            onDelete: onDeleteMember == null
                ? null
                : () => onDeleteMember!(member),
          );
        },
      ),
    );
  }
}
