import 'package:flutter/cupertino.dart';

/// 화면 하단 등에서 쓰는 가로 꽉 찬 기본 버튼.
class AppPrimaryButton extends StatelessWidget {
  const AppPrimaryButton({
    super.key,
    required this.label,
    required this.onPressed,
    this.color = CupertinoColors.activeBlue,
  });

  final String label;

  /// null이면 비활성화된다.
  final VoidCallback? onPressed;
  final Color color;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: double.infinity,
      child: CupertinoButton(
        color: color,
        disabledColor: color.withValues(alpha: 0.4),
        onPressed: onPressed,
        child: Text(
          label,
          style: const TextStyle(
            color: CupertinoColors.white,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }
}
