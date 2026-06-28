import 'package:flutter/cupertino.dart';

import '../adaptive/adaptive_action_sheet.dart';

/// 메인 필터 아이콘에서 띄우는 성별 선택 메뉴.
///
/// iOS는 액션 시트, Android는 Material 바텀시트로 분기한다.
/// 선택한 라벨('전체' / '남자' / '여자')을 반환하며, 닫기/취소 시 null을 반환한다.
class GenderFilterSheet {
  const GenderFilterSheet._();

  static const List<String> _options = ['전체', '남자', '여자'];

  static Future<String?> show(BuildContext context) async {
    String? selected;
    await AdaptiveActionSheet.show(
      context,
      actions: [
        for (final label in _options)
          AdaptiveActionSheetAction(
            label: label,
            onPressed: () => selected = label,
          ),
      ],
    );
    return selected;
  }
}
