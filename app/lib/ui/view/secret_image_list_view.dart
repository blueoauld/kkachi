import 'package:app/model/member_activity.dart';
import 'package:app/router.dart';
import 'package:app/ui/widgets/activity_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

/// 비밀 사진 목록 화면. (현재는 mock 데이터)
class SecretImageListView extends StatefulWidget {
  const SecretImageListView({super.key});

  @override
  State<SecretImageListView> createState() => _SecretImageListViewState();
}

class _SecretImageListViewState extends State<SecretImageListView> {
  /// mock 비밀 사진 회원 목록. (TODO: API 연동)
  final List<MemberActivity> _members = List<MemberActivity>.generate(0, (i) {
    final n = i + 1;
    return MemberActivity(
      profileUrl: i % 5 == 4 ? null : 'https://picsum.photos/seed/secret$n/200',
      nickname: '비밀회원$n',
      gender: i.isEven ? '여자' : '남자',
      age: 20 + (i % 30),
      comment: '멘트 내용이 여기에 표시됩니다.',
    );
  });

  void _onTapMember(MemberActivity member) {
    context.push(
      '${AppRoutes.main}/${AppRoutes.member}',
      extra: member.toMember(),
    );
  }

  void _onDeleteMember(MemberActivity member) {
    setState(() => _members.remove(member));
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('비밀 사진 목록'),
      ),
      child: SafeArea(
        bottom: false,
        child: ActivityList(
          members: _members,
          emptyMessage: '비밀 사진을 공개한 회원이 없습니다.',
          onTapMember: _onTapMember,
          onDeleteMember: _onDeleteMember,
        ),
      ),
    );
  }
}
