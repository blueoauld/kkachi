import 'package:app/model/chat_message.dart';
import 'package:app/model/chat_room.dart';
import 'package:app/model/member.dart';
import 'package:app/router.dart';
import 'package:app/ui/widgets/chat_input_bar.dart';
import 'package:app/ui/widgets/chat_message_bubble.dart';
import 'package:app/ui/widgets/chat_profile_avatar.dart';
import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

/// 채팅방 상세 화면. (현재는 UI 껍데기)
class ChatDetailView extends StatefulWidget {
  const ChatDetailView({super.key, required this.room});

  final ChatRoom room;

  @override
  State<ChatDetailView> createState() => _ChatDetailViewState();
}

class _ChatDetailViewState extends State<ChatDetailView>
    with WidgetsBindingObserver {
  final TextEditingController _inputController = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  /// 스크롤이 (거의) 맨 아래에 있는지 여부.
  bool _atBottom = true;

  /// mock 대화 내용. (TODO: 메시지 API 연동)
  final List<ChatMessage> _messages = List<ChatMessage>.generate(100, (i) {
    final isMine = i.isOdd;
    final hour = 9 + (i ~/ 6); // 9시부터 천천히 증가
    final minute = (i * 7) % 60;
    final isPm = hour >= 12;
    final hour12 = hour > 12 ? hour - 12 : hour;
    final time =
        '${isPm ? '오후' : '오전'} $hour12:${minute.toString().padLeft(2, '0')}';
    return ChatMessage(
      text: isMine
          ? '내가 보낸 메시지 내가 보낸 메시지 내가 보낸 메시지 내가 보낸 메시지 ${i + 1}'
          : '상대가 보낸 메시지 상대가 보낸 메시지 상대가 보낸 메시지 상대가 보낸 메시지 ${i + 1}',
      isMine: isMine,
      time: time,
    );
  });

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    _scrollController.addListener(_onScroll);
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    _scrollController.dispose();
    _inputController.dispose();
    super.dispose();
  }

  void _onScroll() {
    if (!_scrollController.hasClients) return;
    final pos = _scrollController.position;
    // 바닥에서 80px 이내면 '맨 아래'로 간주한다.
    _atBottom = pos.pixels >= pos.maxScrollExtent - 80;
  }

  /// 키보드가 오르내리는 등 화면 메트릭이 바뀔 때 호출된다.
  @override
  void didChangeMetrics() {
    // 키보드가 올라오기 직전 맨 아래였다면, 리스트도 바닥으로 따라 올린다.
    if (!_atBottom) return;
    WidgetsBinding.instance.addPostFrameCallback((_) => _scrollToBottom());
  }

  void _scrollToBottom() {
    if (!_scrollController.hasClients) return;
    _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
  }

  void _send() {
    final text = _inputController.text.trim();
    if (text.isEmpty) return;
    final now = DateTime.now();
    final isPm = now.hour >= 12;
    final hour12 = now.hour > 12
        ? now.hour - 12
        : (now.hour == 0 ? 12 : now.hour);
    final time =
        '${isPm ? '오후' : '오전'} $hour12:${now.minute.toString().padLeft(2, '0')}';
    setState(() {
      _messages.add(ChatMessage(text: text, isMine: true, time: time));
      _inputController.clear();
    });
    // 내가 보낸 메시지는 항상 바닥으로 스크롤.
    WidgetsBinding.instance.addPostFrameCallback((_) => _scrollToBottom());
  }

  /// 프로필을 탭하면 회원 상세 화면으로 이동한다.
  void _openProfile() {
    final room = widget.room;
    // 채팅방 정보로 회원 모델을 구성한다. (TODO: 실제 회원 데이터 연동)
    final member = Member(
      profileUrl: room.profileUrl,
      nickname: room.nickname,
      updatedAt: room.updatedAt,
      gender: '',
      age: 0,
      distance: null,
      comment: '',
      hearts: 0,
    );
    context.push('${AppRoutes.main}/${AppRoutes.member}', extra: member);
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        border: null,
        backgroundColor: CupertinoColors.systemBackground,
        // 헤더 내부 패딩. 좌/우 기본값(16), 하단만 8 (상단은 0 — 고정 높이라 찌부 방지).
        padding: const EdgeInsetsDirectional.fromSTEB(16, 0, 16, 5),
        middle: Text(widget.room.nickname),
        trailing: ChatProfileAvatar(
          url: widget.room.profileUrl,
          onTap: _openProfile,
        ),
      ),
      child: SafeArea(
        child: Column(
          children: [
            Expanded(
              child: GestureDetector(
                // 빈 영역을 탭하면 키보드를 닫는다.
                behavior: HitTestBehavior.translucent,
                onTap: () => FocusScope.of(context).unfocus(),
                child: CupertinoScrollbar(
                  controller: _scrollController,
                  child: ListView.builder(
                    controller: _scrollController,
                    padding: const EdgeInsets.fromLTRB(16, 16, 16, 0),
                    itemCount: _messages.length,
                    itemBuilder: (context, index) =>
                        ChatMessageBubble(message: _messages[index]),
                  ),
                ),
              ),
            ),
            ChatInputBar(
              controller: _inputController,
              onPlus: () {},
              onSend: _send,
            ),
          ],
        ),
      ),
    );
  }
}
