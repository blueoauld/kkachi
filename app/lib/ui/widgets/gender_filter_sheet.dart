import 'dart:io' show Platform;

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart'
    show InkWell, RoundedRectangleBorder, showModalBottomSheet;

/// 메인 필터 아이콘에서 띄우는 성별 선택 메뉴.
///
/// iOS는 [CupertinoActionSheet], Android는 Material 바텀시트로 분기한다.
/// 선택한 라벨('전체' / '남자' / '여자')을 반환하며, 닫기/취소 시 null을 반환한다.
class GenderFilterSheet {
  const GenderFilterSheet._();

  static const List<String> _options = ['전체', '남자', '여자'];

  static Future<String?> show(BuildContext context) {
    return Platform.isAndroid ? _showAndroid(context) : _showIos(context);
  }

  static Future<String?> _showIos(BuildContext context) {
    return showCupertinoModalPopup<String>(
      context: context,
      builder: (context) => CupertinoActionSheet(
        actions: [
          for (final label in _options)
            CupertinoActionSheetAction(
              onPressed: () => Navigator.pop(context, label),
              child: Text(label),
            ),
        ],
        cancelButton: CupertinoActionSheetAction(
          isDefaultAction: true,
          onPressed: () => Navigator.pop(context),
          child: const Text('닫기'),
        ),
      ),
    );
  }

  static Future<String?> _showAndroid(BuildContext context) {
    final backgroundColor = CupertinoDynamicColor.resolve(
      const CupertinoDynamicColor.withBrightness(
        color: CupertinoColors.white,
        darkColor: Color(0xFF1C1C1E),
      ),
      context,
    );
    final textColor = CupertinoColors.label.resolveFrom(context);
    final handleColor = CupertinoColors.systemGrey3.resolveFrom(context);

    return showModalBottomSheet<String>(
      context: context,
      // 하단 탭바 위로 띄워 마지막 항목이 가려지지 않도록 한다.
      useRootNavigator: true,
      backgroundColor: backgroundColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (context) {
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
              for (final label in _options)
                InkWell(
                  onTap: () => Navigator.pop(context, label),
                  child: SizedBox(
                    width: double.infinity,
                    child: Padding(
                      padding: const EdgeInsets.symmetric(
                        vertical: 16,
                        horizontal: 24,
                      ),
                      child: Text(
                        label,
                        style: TextStyle(fontSize: 16, color: textColor),
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
