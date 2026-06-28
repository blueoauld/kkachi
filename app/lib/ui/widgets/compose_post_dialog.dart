import 'package:flutter/cupertino.dart';

import '../adaptive/adaptive_input_dialog.dart';

/// 코멘트 입력 다이얼로그.
///
/// iOS는 CupertinoAlertDialog, Android는 Material AlertDialog로 분기한다.
class ComposePostDialog {
  const ComposePostDialog._();

  static Future<String?> show(BuildContext context) {
    return AdaptiveInputDialog.show(
      context,
      title: '코멘트',
      placeholder: '내용 입력',
      confirmLabel: '작성',
      maxLength: 100,
    );
  }
}
