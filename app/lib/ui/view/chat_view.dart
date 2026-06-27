import 'package:flutter/cupertino.dart';

class ChatView extends StatelessWidget {
  const ChatView({super.key});

  @override
  Widget build(BuildContext context) {
    return const CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(middle: Text('채팅')),
      child: Center(child: Text('채팅 화면', style: TextStyle(fontSize: 28))),
    );
  }
}
