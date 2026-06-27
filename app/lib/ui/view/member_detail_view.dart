import 'package:app/model/member.dart';
import 'package:flutter/cupertino.dart';

class MemberDetailView extends StatelessWidget {
  const MemberDetailView({super.key, required this.member});

  final Member member;

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(middle: Text(member.nickname)),
      child: const Center(child: Text('Hello World')),
    );
  }
}
