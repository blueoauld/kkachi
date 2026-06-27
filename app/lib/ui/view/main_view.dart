import 'package:app/model/member.dart';
import 'package:app/router.dart';
import 'package:app/ui/widgets/compose_post_dialog.dart';
import 'package:app/ui/widgets/gender_filter_sheet.dart';
import 'package:app/ui/widgets/location_filter_bar.dart';
import 'package:app/ui/widgets/main_app_bar.dart';
import 'package:app/ui/widgets/member_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

class MainView extends StatefulWidget {
  const MainView({super.key});

  @override
  State<MainView> createState() => _MainViewState();
}

class _MainViewState extends State<MainView> {
  int _segment = 0;

  static const List<Member> _members = [
    Member(
      profileUrl: 'https://picsum.photos/seed/1/200',
      nickname: '닉네임1',
      updatedAt: '방금 전',
      gender: '여자',
      age: 25,
      distance: 1.2,
      comment:
          '멘트 내용이 여기에 표시됩니다. 멘트 내용이 여기에 표시됩니다. 멘트 내용이 여기에 표시됩니다. 멘트 내용이 여기에 표시됩니다. 멘트 내용이 여기에 표시됩니다. 멘트 내용이 여기에 표시됩니다.',
      hearts: 100,
    ),
    Member(
      profileUrl: 'https://picsum.photos/seed/2/200',
      nickname: '닉네임2',
      updatedAt: '5분 전',
      gender: '남자',
      age: 28,
      distance: 3.4,
      comment: '멘트 내용이 여기에 표시됩니다.',
      hearts: 57,
    ),
    Member(
      profileUrl: null,
      nickname: '닉네임3',
      updatedAt: '1시간 전',
      gender: '여자',
      age: 31,
      distance: null,
      comment: '멘트 내용이 여기에 표시됩니다.',
      hearts: 23,
    ),
  ];

  Future<void> _handleCompose(BuildContext context) async {
    final text = await ComposePostDialog.show(context);
    if (text == null || !mounted) return;
    setState(() {});
  }

  Future<void> _handleRefresh() async {
    await Future<void>.delayed(const Duration(seconds: 1));
    if (!mounted) return;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: MainAppBar(
        onSearch: () {},
        onFilter: () => GenderFilterSheet.show(context),
        onCompose: () => _handleCompose(context),
      ),
      child: SafeArea(
        child: Column(
          children: [
            LocationFilterBar(
              value: _segment,
              onChanged: (v) => setState(() => _segment = v),
            ),
            Expanded(
              child: MemberList(
                members: _members,
                onRefresh: _handleRefresh,
                onTapMember: (member) => context.push(
                  '${AppRoutes.main}/${AppRoutes.member}',
                  extra: member,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
