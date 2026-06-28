import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

import '../../router.dart';
import '../adaptive/adaptive_action_sheet.dart';

/// 프로필 디테일 우상단 메뉴.
///
/// iOS는 액션 시트, Android는 Material 바텀시트로 분기한다.
class MemberDetailMenuSheet {
  const MemberDetailMenuSheet._();

  static Future<void> show(BuildContext context, {required String nickname}) {
    return AdaptiveActionSheet.show(
      context,
      actions: [
        AdaptiveActionSheetAction(label: '비밀 사진 공개', onPressed: () {}),
        AdaptiveActionSheetAction(
          label: '신고하기',
          isDestructive: true,
          onPressed: () => context.push(
            '${AppRoutes.main}/${AppRoutes.member}/${AppRoutes.report}',
            extra: nickname,
          ),
        ),
      ],
    );
  }
}
