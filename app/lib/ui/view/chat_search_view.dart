import 'package:app/model/chat_room.dart';
import 'package:app/ui/widgets/chat_list.dart';
import 'package:flutter/cupertino.dart';

/// 채팅 검색 화면. (현재는 UI 껍데기)
///
/// 검색 결과 리스트는 채팅 화면과 동일한 [ChatList]를 사용한다.
class ChatSearchView extends StatefulWidget {
  const ChatSearchView({super.key});

  @override
  State<ChatSearchView> createState() => _ChatSearchViewState();
}

class _ChatSearchViewState extends State<ChatSearchView> {
  final TextEditingController _queryController = TextEditingController();

  /// 검색 대상 mock 채팅방 목록. (TODO: 검색 API 연동)
  static final List<ChatRoom> _mockRooms = List<ChatRoom>.generate(30, (i) {
    final n = i + 1;
    return ChatRoom(
      profileUrl: i % 5 == 4 ? null : 'https://picsum.photos/seed/chat$n/200',
      nickname: '닉네임$n',
      lastMessage: '마지막 메시지 내용이 여기에 표시됩니다.',
      updatedAt: '$n분 전',
      unreadCount: i % 3 == 0 ? (i % 7) + 1 : 0,
    );
  });

  /// 검색 결과.
  List<ChatRoom> _results = const [];

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
          : _mockRooms
                .where((room) => room.nickname.contains(keyword))
                .toList();
    });
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      resizeToAvoidBottomInset: false,
      navigationBar: const CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('채팅 검색'),
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
                  : ChatList(rooms: _results, onTapRoom: (room) {}),
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
