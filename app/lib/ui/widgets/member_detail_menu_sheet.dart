import 'package:flutter/cupertino.dart';

class MemberDetailMenuSheet extends StatelessWidget {
  const MemberDetailMenuSheet({super.key});

  static Future<void> show(BuildContext context) {
    return showCupertinoModalPopup<void>(
      context: context,
      builder: (context) => const MemberDetailMenuSheet(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoActionSheet(
      actions: [
        CupertinoActionSheetAction(
          isDestructiveAction: true,
          onPressed: () => Navigator.pop(context),
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
