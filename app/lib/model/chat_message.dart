class ChatMessage {
  const ChatMessage({
    required this.text,
    required this.isMine,
    required this.time,
  });

  final String text;

  /// 내가 보낸 메시지면 true (오른쪽 파란색), 상대가 보낸 메시지면 false (왼쪽 회색).
  final bool isMine;

  /// 보낸 시간. (예: '오후 2:30')
  final String time;
}
