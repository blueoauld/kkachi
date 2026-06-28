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

  static final List<Member> _members = List<Member>.generate(1000, (i) {
    final n = i + 1;
    return Member(
      profileUrl: i % 5 == 4 ? null : 'https://picsum.photos/seed/$n/200',
      nickname: '닉네임$n',
      updatedAt: '$n분 전',
      gender: i.isEven ? '여자' : '남자',
      age: 20 + (i % 30),
      distance: i % 7 == 0 ? null : (i % 100) / 10 + 0.1,
      comment: '멘트 내용이 여기에 표시됩니다.',
      hearts: (i * 7) % 200,
    );
  });

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
      resizeToAvoidBottomInset: false,
      navigationBar: MainAppBar(
        onSearch: () => context.push('${AppRoutes.main}/${AppRoutes.search}'),
        onFilter: () => GenderFilterSheet.show(context),
        onCompose: () => _handleCompose(context),
      ),
      child: SafeArea(
        bottom: false,
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
