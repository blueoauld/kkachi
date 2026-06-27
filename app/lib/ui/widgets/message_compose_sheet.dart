import 'package:flutter/cupertino.dart';

class MessageComposeSheet extends StatefulWidget {
  const MessageComposeSheet({super.key});

  static Future<String?> show(BuildContext context) {
    return showCupertinoDialog<String>(
      context: context,
      barrierDismissible: true,
      builder: (context) => const MessageComposeSheet(),
    );
  }

  @override
  State<MessageComposeSheet> createState() => _MessageComposeSheetState();
}

class _MessageComposeSheetState extends State<MessageComposeSheet> {
  final TextEditingController _controller = TextEditingController();
  bool _canSend = false;

  @override
  void initState() {
    super.initState();
    _controller.addListener(_handleChanged);
  }

  void _handleChanged() {
    final canSend = _controller.text.trim().isNotEmpty;
    if (canSend != _canSend) {
      setState(() => _canSend = canSend);
    }
  }

  @override
  void dispose() {
    _controller.removeListener(_handleChanged);
    _controller.dispose();
    super.dispose();
  }

  void _send() {
    final text = _controller.text.trim();
    if (text.isEmpty) return;
    Navigator.of(context).pop(text);
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoAlertDialog(
      title: const Text('쪽지'),
      content: Padding(
        padding: const EdgeInsets.only(top: 8),
        child: CupertinoTextField(
          controller: _controller,
          autofocus: true,
          minLines: 1,
          maxLines: 5,
          maxLength: 500,
          textAlignVertical: TextAlignVertical.top,
          placeholder: '내용 입력 (15P)',
        ),
      ),
      actions: [
        CupertinoDialogAction(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('닫기'),
        ),
        CupertinoDialogAction(
          isDefaultAction: true,
          onPressed: _canSend ? _send : null,
          child: const Text('전송'),
        ),
      ],
    );
  }
}
