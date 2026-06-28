import 'dart:io' show Platform;

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart' show AlertDialog, TextButton, showDialog;

/// 확인/취소를 받는 다이얼로그.
///
/// iOS는 [CupertinoAlertDialog], Android는 Material [AlertDialog]로 분기한다.
/// 확인 시 true, 닫기/취소 시 false 또는 null을 반환한다.
class AdaptiveConfirmDialog {
  const AdaptiveConfirmDialog._();

  static Future<bool?> show(
    BuildContext context, {
    required String title,
    String? message,
    required String confirmLabel,
    String cancelLabel = '닫기',
    bool isDestructive = false,
  }) {
    return Platform.isAndroid
        ? _showAndroid(
            context,
            title: title,
            message: message,
            confirmLabel: confirmLabel,
            cancelLabel: cancelLabel,
            isDestructive: isDestructive,
          )
        : _showIos(
            context,
            title: title,
            message: message,
            confirmLabel: confirmLabel,
            cancelLabel: cancelLabel,
            isDestructive: isDestructive,
          );
  }

  /// 버튼이 하나뿐인 안내용 알림.
  ///
  /// [isTitleDestructive]가 true면 제목을 빨간색으로 표시한다.
  static Future<void> alert(
    BuildContext context, {
    required String title,
    String? message,
    String confirmLabel = '닫기',
    bool isTitleDestructive = false,
  }) {
    final titleColor = isTitleDestructive
        ? CupertinoColors.systemRed.resolveFrom(context)
        : CupertinoColors.label.resolveFrom(context);

    if (!Platform.isAndroid) {
      return showCupertinoDialog<void>(
        context: context,
        builder: (context) => CupertinoAlertDialog(
          title: Text(title, style: TextStyle(color: titleColor)),
          content: message == null ? null : Text(message),
          actions: [
            CupertinoDialogAction(
              isDefaultAction: true,
              onPressed: () => Navigator.of(context).pop(),
              child: Text(confirmLabel),
            ),
          ],
        ),
      );
    }

    final backgroundColor = CupertinoDynamicColor.resolve(
      const CupertinoDynamicColor.withBrightness(
        color: CupertinoColors.white,
        darkColor: Color(0xFF2C2C2E),
      ),
      context,
    );
    final secondaryColor = CupertinoColors.secondaryLabel.resolveFrom(context);
    final accent = CupertinoColors.activeBlue.resolveFrom(context);

    return showDialog<void>(
      context: context,
      useRootNavigator: true,
      builder: (context) => AlertDialog(
        backgroundColor: backgroundColor,
        title: Text(title, style: TextStyle(color: titleColor)),
        content: message == null
            ? null
            : Text(message, style: TextStyle(color: secondaryColor)),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: Text(confirmLabel, style: TextStyle(color: accent)),
          ),
        ],
      ),
    );
  }

  static Future<bool?> _showIos(
    BuildContext context, {
    required String title,
    required String? message,
    required String confirmLabel,
    required String cancelLabel,
    required bool isDestructive,
  }) {
    return showCupertinoDialog<bool>(
      context: context,
      builder: (context) => CupertinoAlertDialog(
        title: Text(title),
        content: message == null
            ? null
            : Padding(
                padding: const EdgeInsets.only(top: 8),
                child: Text(message),
              ),
        actions: [
          CupertinoDialogAction(
            onPressed: () => Navigator.pop(context, false),
            child: Text(cancelLabel),
          ),
          CupertinoDialogAction(
            isDestructiveAction: isDestructive,
            onPressed: () => Navigator.pop(context, true),
            child: Text(confirmLabel),
          ),
        ],
      ),
    );
  }

  static Future<bool?> _showAndroid(
    BuildContext context, {
    required String title,
    required String? message,
    required String confirmLabel,
    required String cancelLabel,
    required bool isDestructive,
  }) {
    final backgroundColor = CupertinoDynamicColor.resolve(
      const CupertinoDynamicColor.withBrightness(
        color: CupertinoColors.white,
        darkColor: Color(0xFF2C2C2E),
      ),
      context,
    );
    final textColor = CupertinoColors.label.resolveFrom(context);
    final secondaryColor = CupertinoColors.secondaryLabel.resolveFrom(context);
    final accent = CupertinoColors.activeBlue.resolveFrom(context);
    final destructiveColor = CupertinoColors.systemRed.resolveFrom(context);

    return showDialog<bool>(
      context: context,
      useRootNavigator: true,
      builder: (context) => AlertDialog(
        backgroundColor: backgroundColor,
        title: Text(title, style: TextStyle(color: textColor)),
        content: message == null
            ? null
            : Text(message, style: TextStyle(color: secondaryColor)),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context, false),
            child: Text(cancelLabel, style: TextStyle(color: textColor)),
          ),
          TextButton(
            onPressed: () => Navigator.pop(context, true),
            child: Text(
              confirmLabel,
              style: TextStyle(
                color: isDestructive ? destructiveColor : accent,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
