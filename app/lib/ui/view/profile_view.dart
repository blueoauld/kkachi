import 'package:flutter/cupertino.dart';

class ProfileView extends StatelessWidget {
  const ProfileView({super.key});

  @override
  Widget build(BuildContext context) {
    return const CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('프로필'),
      ),
      child: Center(child: Text('프로필 화면', style: TextStyle(fontSize: 28))),
    );
  }
}
