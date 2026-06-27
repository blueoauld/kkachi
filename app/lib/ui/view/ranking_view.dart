import 'package:flutter/cupertino.dart';

class RankingView extends StatelessWidget {
  const RankingView({super.key});

  @override
  Widget build(BuildContext context) {
    return const CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('랭킹'),
      ),
      child: Center(child: Text('랭킹 화면', style: TextStyle(fontSize: 28))),
    );
  }
}
