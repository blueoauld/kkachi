import 'package:flutter/cupertino.dart';

class GenderFilterSheet extends StatelessWidget {
  const GenderFilterSheet({super.key});

  static Future<void> show(BuildContext context) {
    return showCupertinoModalPopup<void>(
      context: context,
      builder: (context) => const GenderFilterSheet(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoActionSheet(
      actions: [
        _genderAction(context, '전체'),
        _genderAction(context, '남자'),
        _genderAction(context, '여자'),
      ],
      cancelButton: CupertinoActionSheetAction(
        isDefaultAction: true,
        onPressed: () => Navigator.pop(context),
        child: const Text('닫기'),
      ),
    );
  }

  CupertinoActionSheetAction _genderAction(BuildContext context, String label) {
    return CupertinoActionSheetAction(
      onPressed: () => Navigator.pop(context),
      child: Text(label),
    );
  }
}
