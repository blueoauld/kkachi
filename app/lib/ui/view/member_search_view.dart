import 'package:app/model/member.dart';
import 'package:app/router.dart';
import 'package:app/ui/widgets/member_search_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

/// 회원 검색 화면. (현재는 UI 껍데기)
///
/// 검색 결과 리스트는 메인 화면과 동일한 [Member] 모델을 사용한다.
class MemberSearchView extends StatefulWidget {
  const MemberSearchView({super.key});

  @override
  State<MemberSearchView> createState() => _MemberSearchViewState();
}

class _MemberSearchViewState extends State<MemberSearchView> {
  final TextEditingController _queryController = TextEditingController();

  /// 검색 대상 mock 회원 목록. (TODO: 검색 API 연동)
  static final List<Member> _mockMembers = List<Member>.generate(100, (i) {
    final n = i + 1;
    return Member(
      profileUrl: i % 5 == 4 ? null : 'https://picsum.photos/seed/search$n/200',
      nickname: '검색회원$n',
      updatedAt: '$n분 전',
      gender: i.isEven ? '여자' : '남자',
      age: 20 + (i % 30),
      distance: i % 7 == 0 ? null : (i % 100) / 10 + 0.1,
      comment: '멘트 내용이 여기에 표시됩니다.',
      hearts: (i * 7) % 200,
    );
  });

  /// 검색 결과.
  List<Member> _results = const [];

  @override
  void dispose() {
    _queryController.dispose();
    super.dispose();
  }

  void _onQueryChanged(String query) {
    // TODO: 검색 API 연동. 현재는 mock 목록을 닉네임으로 필터링.
    final keyword = query.trim();
    setState(() {
      _results = keyword.isEmpty
          ? const []
          : _mockMembers
                .where((member) => member.nickname.contains(keyword))
                .toList();
    });
  }

  void _onTapMember(Member member) {
    context.push('${AppRoutes.main}/${AppRoutes.member}', extra: member);
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      resizeToAvoidBottomInset: false,
      navigationBar: const CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('회원 검색'),
      ),
      child: SafeArea(
        bottom: false,
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
              child: CupertinoSearchTextField(
                controller: _queryController,
                placeholder: '닉네임 (2자 이상)',
                onChanged: _onQueryChanged,
                onSubmitted: _onQueryChanged,
              ),
            ),
            Expanded(
              child: _results.isEmpty
                  ? const _EmptyResult()
                  : MemberSearchList(
                      members: _results,
                      onTapMember: _onTapMember,
                    ),
            ),
          ],
        ),
      ),
    );
  }
}

class _EmptyResult extends StatelessWidget {
  const _EmptyResult();

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text(
        '검색 결과가 없습니다.',
        style: TextStyle(
          color: CupertinoColors.secondaryLabel.resolveFrom(context),
        ),
      ),
    );
  }
}
