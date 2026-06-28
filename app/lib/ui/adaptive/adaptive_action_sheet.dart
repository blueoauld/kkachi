import 'dart:io' show Platform;

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart'
    show InkWell, RoundedRectangleBorder, showModalBottomSheet;

/// [AdaptiveActionSheet]의 액션 항목.
class AdaptiveActionSheetAction {
  const AdaptiveActionSheetAction({
    required this.label,
    required this.onPressed,
    this.isDestructive = false,
  });

  final String label;

  /// 시트가 닫힌 뒤 호출된다.
  final VoidCallback onPressed;

  /// 파괴적(빨간색) 액션 여부.
  final bool isDestructive;
}

/// 플랫폼별로 분기되는 액션 시트.
///
/// iOS는 [CupertinoActionSheet], Android는 Material 바텀시트로 분기한다.
/// 다크모드를 지원하며, 하단 탭바에 가려지지 않도록 루트 네비게이터에 띄운다.
class AdaptiveActionSheet {
  const AdaptiveActionSheet._();

  static Future<void> show(
    BuildContext context, {
    required List<AdaptiveActionSheetAction> actions,
    String cancelLabel = '닫기',
  }) {
    return Platform.isAndroid
        ? _showAndroid(context, actions: actions)
        : _showIos(context, actions: actions, cancelLabel: cancelLabel);
  }

  static Future<void> _showIos(
    BuildContext context, {
    required List<AdaptiveActionSheetAction> actions,
    required String cancelLabel,
  }) {
    return showCupertinoModalPopup<void>(
      context: context,
      builder: (sheetContext) => CupertinoActionSheet(
        actions: [
          for (final action in actions)
            CupertinoActionSheetAction(
              isDestructiveAction: action.isDestructive,
              onPressed: () {
                Navigator.pop(sheetContext);
                action.onPressed();
              },
              child: Text(action.label),
            ),
        ],
        cancelButton: CupertinoActionSheetAction(
          isDefaultAction: true,
          onPressed: () => Navigator.pop(sheetContext),
          child: Text(cancelLabel),
        ),
      ),
    );
  }

  static Future<void> _showAndroid(
    BuildContext context, {
    required List<AdaptiveActionSheetAction> actions,
  }) {
    final backgroundColor = CupertinoDynamicColor.resolve(
      const CupertinoDynamicColor.withBrightness(
        color: CupertinoColors.white,
        darkColor: Color(0xFF1C1C1E),
      ),
      context,
    );
    final labelColor = CupertinoColors.label.resolveFrom(context);
    final destructiveColor = CupertinoColors.systemRed.resolveFrom(context);
    final handleColor = CupertinoColors.systemGrey3.resolveFrom(context);

    return showModalBottomSheet<void>(
      context: context,
      // 하단 탭바 위로 띄워 항목이 가려지지 않도록 한다.
      useRootNavigator: true,
      backgroundColor: backgroundColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (sheetContext) {
        return SafeArea(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                margin: const EdgeInsets.symmetric(vertical: 12),
                width: 36,
                height: 4,
                decoration: BoxDecoration(
                  color: handleColor,
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
              for (final action in actions)
                InkWell(
                  onTap: () {
                    Navigator.pop(sheetContext);
                    action.onPressed();
                  },
                  child: SizedBox(
                    width: double.infinity,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(
                        vertical: 16,
                        horizontal: 24,
                      ),
                      child: Text(
                        action.label,
                        style: TextStyle(
                          fontSize: 16,
                          color: action.isDestructive
                              ? destructiveColor
                              : labelColor,
                        ),
                      ),
                    ),
                  ),
                ),
              const SizedBox(height: 8),
            ],
          ),
        );
      },
    );
  }
}
