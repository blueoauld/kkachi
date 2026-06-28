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

/// 텍스트 한 건을 입력받는 다이얼로그.
///
/// iOS는 [CupertinoAlertDialog], Android는 Material [AlertDialog]로 분기한다.
/// 입력한 텍스트(trim)를 반환하며, 닫기/취소 시 null을 반환한다.
class AdaptiveInputDialog extends StatefulWidget {
  const AdaptiveInputDialog._({
    required this.title,
    required this.placeholder,
    required this.confirmLabel,
    required this.cancelLabel,
    this.maxLength,
    this.maxLines = 5,
  });

  final String title;
  final String placeholder;
  final String confirmLabel;
  final String cancelLabel;
  final int? maxLength;
  final int maxLines;

  static Future<String?> show(
    BuildContext context, {
    required String title,
    required String placeholder,
    required String confirmLabel,
    String cancelLabel = '닫기',
    int? maxLength,
    int maxLines = 5,
  }) {
    AdaptiveInputDialog builder(BuildContext _) => AdaptiveInputDialog._(
      title: title,
      placeholder: placeholder,
      confirmLabel: confirmLabel,
      cancelLabel: cancelLabel,
      maxLength: maxLength,
      maxLines: maxLines,
    );

    return Platform.isAndroid
        ? showDialog<String>(
            context: context,
            useRootNavigator: true,
            builder: builder,
          )
        : showCupertinoDialog<String>(
            context: context,
            barrierDismissible: true,
            builder: builder,
          );
  }

  @override
  State<AdaptiveInputDialog> createState() => _AdaptiveInputDialogState();
}

class _AdaptiveInputDialogState extends State<AdaptiveInputDialog> {
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
      title: Text(widget.title),
      content: Padding(
        padding: const EdgeInsets.only(top: 8),
        child: CupertinoTextField(
          controller: _controller,
          autofocus: true,
          minLines: 1,
          maxLines: widget.maxLines,
          maxLength: widget.maxLength,
          textAlignVertical: TextAlignVertical.top,
          placeholder: widget.placeholder,
          onSubmitted: (_) => _submit(),
        ),
      ),
      actions: [
        CupertinoDialogAction(
          onPressed: () => Navigator.of(context).pop(),
          child: Text(widget.cancelLabel),
        ),
        CupertinoDialogAction(
          isDefaultAction: true,
          onPressed: _canSubmit ? _submit : null,
          child: Text(widget.confirmLabel),
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
      title: Text(widget.title, style: TextStyle(color: textColor)),
      content: TextField(
        controller: _controller,
        autofocus: true,
        minLines: 1,
        maxLines: widget.maxLines,
        maxLength: widget.maxLength,
        style: TextStyle(color: textColor),
        cursorColor: accent,
        onSubmitted: (_) => _submit(),
        decoration: InputDecoration(
          hintText: widget.placeholder,
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
          child: Text(widget.cancelLabel, style: TextStyle(color: textColor)),
        ),
        TextButton(
          onPressed: _canSubmit ? _submit : null,
          child: Text(
            widget.confirmLabel,
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
