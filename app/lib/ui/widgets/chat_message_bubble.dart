import 'package:app/model/chat_message.dart';
import 'package:flutter/cupertino.dart';

/// 말풍선. 내 메시지는 오른쪽 파란색, 상대 메시지는 왼쪽 회색.
class ChatMessageBubble extends StatelessWidget {
  const ChatMessageBubble({super.key, required this.message});

  final ChatMessage message;

  @override
  Widget build(BuildContext context) {
    final isMine = message.isMine;
    final bubbleColor = isMine
        ? CupertinoColors.activeBlue.resolveFrom(context)
        : CupertinoColors.systemGrey5.resolveFrom(context);
    final textColor = isMine
        ? CupertinoColors.white
        : CupertinoColors.label.resolveFrom(context);

    final timeLabel = Text(
      message.time,
      style: TextStyle(
        fontSize: 11,
        color: CupertinoColors.secondaryLabel.resolveFrom(context),
      ),
    );

    final bubble = Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 12),
      constraints: BoxConstraints(
        maxWidth: MediaQuery.of(context).size.width * 0.72,
      ),
      decoration: BoxDecoration(
        color: bubbleColor,
        borderRadius: BorderRadius.circular(18),
      ),
      child: Text(
        message.text,
        style: TextStyle(fontSize: 15, color: textColor),
      ),
    );

    // 내 메시지는 시간이 말풍선 왼쪽, 상대 메시지는 오른쪽에 붙는다.
    final children = isMine
        ? [timeLabel, const SizedBox(width: 6), bubble]
        : [bubble, const SizedBox(width: 6), timeLabel];

    return Container(
      margin: const EdgeInsets.symmetric(vertical: 4),
      child: Row(
        mainAxisAlignment: isMine
            ? MainAxisAlignment.end
            : MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: children,
      ),
    );
  }
}
