import 'package:flutter/cupertino.dart';

class ComposePostDialog extends StatefulWidget {
  const ComposePostDialog({super.key});

  static Future<String?> show(BuildContext context) {
    return showCupertinoDialog<String>(
      context: context,
      barrierDismissible: true,
      builder: (context) => const ComposePostDialog(),
    );
  }

  @override
  State<ComposePostDialog> createState() => _ComposePostDialogState();
}

class _ComposePostDialogState extends State<ComposePostDialog> {
  final TextEditingController _controller = TextEditingController();
  bool _canSubmit = false;

  @override
  void initState() {
    super.initState();
    _controller.addListener(_handleChanged);
  }

  void _handleChanged() {
    final canSubmit = _controller.text.trim().isNotEmpty;
    if (canSubmit != _canSubmit) {
      setState(() => _canSubmit = canSubmit);
    }
  }

  @override
  void dispose() {
    _controller.removeListener(_handleChanged);
    _controller.dispose();
    super.dispose();
  }

  void _submit() {
    final text = _controller.text.trim();
    if (text.isEmpty) return;
    Navigator.of(context).pop(text);
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoAlertDialog(
      title: const Text('코멘트'),
      content: Padding(
        padding: const EdgeInsets.only(top: 8),
        child: CupertinoTextField(
          controller: _controller,
          autofocus: true,
          minLines: 1,
          maxLines: 5,
          textAlignVertical: TextAlignVertical.top,
          placeholder: '내용 입력',
          onSubmitted: (_) => _submit(),
        ),
      ),
      actions: [
        CupertinoDialogAction(
          onPressed: () => Navigator.of(context).pop(),
          child: const Text('닫기'),
        ),
        CupertinoDialogAction(
          isDefaultAction: true,
          onPressed: _canSubmit ? _submit : null,
          child: const Text('작성'),
        ),
      ],
    );
  }
}
