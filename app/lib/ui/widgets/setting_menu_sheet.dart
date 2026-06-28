import 'package:flutter/cupertino.dart';

import '../adaptive/adaptive_action_sheet.dart';

/// 설정 화면 우상단 메뉴.
///
/// iOS는 액션 시트, Android는 Material 바텀시트로 분기한다.
class SettingMenuSheet {
  const SettingMenuSheet._();

  static Future<void> show(BuildContext context) {
    return AdaptiveActionSheet.show(
      context,
      actions: [
        AdaptiveActionSheetAction(label: '로그아웃', onPressed: () {}),
        AdaptiveActionSheetAction(
          label: '회원탈퇴',
          isDestructive: true,
          onPressed: () {},
        ),
      ],
    );
  }
}
