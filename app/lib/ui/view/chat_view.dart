import 'package:app/model/chat_room.dart';
import 'package:app/router.dart';
import 'package:app/ui/widgets/chat_app_bar.dart';
import 'package:app/ui/widgets/chat_filter_bar.dart';
import 'package:app/ui/widgets/chat_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

class ChatView extends StatefulWidget {
  const ChatView({super.key});

  @override
  State<ChatView> createState() => _ChatViewState();
}

class _ChatViewState extends State<ChatView> {
  int _segment = 0;

  static final List<ChatRoom> _rooms = List<ChatRoom>.generate(30, (i) {
    final n = i + 1;
    return ChatRoom(
      profileUrl: i % 5 == 4 ? null : 'https://picsum.photos/seed/$n/200',
      nickname: '닉네임$n',
      lastMessage: '마지막 메시지 내용이 여기에 표시됩니다.',
      updatedAt: '$n분 전',
      unreadCount: i % 3 == 0 ? (i % 7) + 1 : 0,
    );
  });

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      resizeToAvoidBottomInset: false,
      navigationBar: ChatAppBar(
        onSearch: () => context.push('${AppRoutes.chat}/${AppRoutes.search}'),
        onNotification: () {},
      ),
      child: SafeArea(
        bottom: false,
        child: Column(
          children: [
            ChatFilterBar(
              value: _segment,
              onChanged: (v) => setState(() => _segment = v),
            ),
            Expanded(
              child: ChatList(rooms: _rooms, onTapRoom: (room) {}),
            ),
          ],
        ),
      ),
    );
  }
}
