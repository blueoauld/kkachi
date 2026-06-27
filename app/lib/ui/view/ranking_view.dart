import 'package:app/model/member.dart';
import 'package:app/router.dart';
import 'package:app/ui/widgets/gender_filter_bar.dart';
import 'package:app/ui/widgets/member_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

class RankingView extends StatefulWidget {
  const RankingView({super.key});

  @override
  State<RankingView> createState() => _RankingViewState();
}

class _RankingViewState extends State<RankingView> {
  /// 0: 전체, 1: 남자, 2: 여자
  int _segment = 0;

  static final List<Member> _members = List<Member>.generate(1000, (i) {
    final n = i + 1;
    return Member(
      profileUrl: i % 5 == 4 ? null : 'https://picsum.photos/seed/rank$n/200',
      nickname: '닉네임$n',
      updatedAt: '$n분 전',
      gender: i.isEven ? '여자' : '남자',
      age: 20 + (i % 30),
      distance: i % 7 == 0 ? null : (i % 100) / 10 + 0.1,
      comment: '멘트 내용이 여기에 표시됩니다.',
      hearts: (i * 7) % 200,
    );
  });

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      resizeToAvoidBottomInset: false,
      navigationBar: const CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('랭킹'),
      ),
      child: SafeArea(
        bottom: false,
        child: Column(
          children: [
            GenderFilterBar(
              value: _segment,
              onChanged: (v) => setState(() => _segment = v),
            ),
            Expanded(
              child: MemberList(
                members: _members,
                ranked: true,
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
