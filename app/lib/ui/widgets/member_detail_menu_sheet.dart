import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

import '../../router.dart';

class MemberDetailMenuSheet extends StatelessWidget {
  const MemberDetailMenuSheet({super.key, required this.nickname});

  final String nickname;

  static Future<void> show(BuildContext context, {required String nickname}) {
    return showCupertinoModalPopup<void>(
      context: context,
      builder: (context) => MemberDetailMenuSheet(nickname: nickname),
    );
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoActionSheet(
      actions: [
        CupertinoActionSheetAction(
          isDestructiveAction: true,
          onPressed: () {
            Navigator.pop(context);
            context.push(
              '${AppRoutes.main}/${AppRoutes.member}/${AppRoutes.report}',
              extra: nickname,
            );
          },
          child: const Text('신고하기'),
        ),
        CupertinoActionSheetAction(
          onPressed: () => Navigator.pop(context),
          child: const Text('비밀 사진 공개'),
        ),
      ],
      cancelButton: CupertinoActionSheetAction(
        isDefaultAction: true,
        onPressed: () => Navigator.pop(context),
        child: const Text('닫기'),
      ),
    );
  }
}
