import 'package:flutter/cupertino.dart';

import '../adaptive/adaptive_input_dialog.dart';

/// 쪽지 작성 다이얼로그.
///
/// iOS는 CupertinoAlertDialog, Android는 Material AlertDialog로 분기한다.
class MessageComposeSheet {
  const MessageComposeSheet._();

  static Future<String?> show(BuildContext context) {
    return AdaptiveInputDialog.show(
      context,
      title: '쪽지',
      placeholder: '내용 입력 (15P)',
      confirmLabel: '전송',
      maxLength: 500,
    );
  }
}
