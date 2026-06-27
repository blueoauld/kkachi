class ChatRoom {
  const ChatRoom({
    required this.profileUrl,
    required this.nickname,
    required this.lastMessage,
    required this.updatedAt,
    required this.unreadCount,
  });

  final String? profileUrl;
  final String nickname;
  final String lastMessage;
  final String updatedAt;
  final int unreadCount;

  bool get hasUnread => unreadCount > 0;
}
