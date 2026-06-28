import 'dart:io' show Platform;

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart'
    show
        AlertDialog,
        InputDecoration,
        TextButton,
        TextField,
        UnderlineInputBorder,
        showDialog;

/// 코멘트 입력 다이얼로그.
///
/// iOS는 [CupertinoAlertDialog], Android는 Material [AlertDialog]로 분기한다.
/// 작성한 텍스트를 반환하며, 닫기/취소 시 null을 반환한다.
class ComposePostDialog extends StatefulWidget {
  const ComposePostDialog({super.key});

  static Future<String?> show(BuildContext context) {
    return Platform.isAndroid ? _showAndroid(context) : _showIos(context);
  }

  static Future<String?> _showIos(BuildContext context) {
    return showCupertinoDialog<String>(
      context: context,
      barrierDismissible: true,
      builder: (context) => const ComposePostDialog(),
    );
  }

  static Future<String?> _showAndroid(BuildContext context) {
    return showDialog<String>(
      context: context,
      useRootNavigator: true,
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
    return Platform.isAndroid ? _buildAndroid(context) : _buildIos(context);
  }

  Widget _buildIos(BuildContext context) {
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

  Widget _buildAndroid(BuildContext context) {
    final backgroundColor = CupertinoDynamicColor.resolve(
      const CupertinoDynamicColor.withBrightness(
        color: CupertinoColors.white,
        darkColor: Color(0xFF2C2C2E),
      ),
      context,
    );
    final textColor = CupertinoColors.label.resolveFrom(context);
    final hintColor = CupertinoColors.placeholderText.resolveFrom(context);
    final accent = CupertinoColors.activeBlue.resolveFrom(context);

    return AlertDialog(
      backgroundColor: backgroundColor,
      title: Text('코멘트', style: TextStyle(color: textColor)),
      content: TextField(
        controller: _controller,
        autofocus: true,
        minLines: 1,
        maxLines: 5,
        style: TextStyle(color: textColor),
        cursorColor: accent,
        onSubmitted: (_) => _submit(),
        decoration: InputDecoration(
          hintText: '내용 입력',
          hintStyle: TextStyle(color: hintColor),
          enabledBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: hintColor),
          ),
          focusedBorder: UnderlineInputBorder(
            borderSide: BorderSide(color: accent),
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(),
          child: Text('닫기', style: TextStyle(color: textColor)),
        ),
        TextButton(
          onPressed: _canSubmit ? _submit : null,
          child: Text(
            '작성',
            style: TextStyle(
              color: _canSubmit
                  ? accent
                  : CupertinoColors.inactiveGray.resolveFrom(context),
            ),
          ),
        ),
      ],
    );
  }
}
