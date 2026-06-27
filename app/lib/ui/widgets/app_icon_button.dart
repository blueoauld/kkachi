import 'package:flutter/cupertino.dart';

class AppIconButton extends StatelessWidget {
  const AppIconButton({
    super.key,
    required this.icon,
    required this.onPressed,
    this.size = 28,
    this.color,
  });

  final IconData icon;
  final VoidCallback onPressed;
  final double size;
  final Color? color;

  @override
  Widget build(BuildContext context) {
    return CupertinoButton(
      padding: EdgeInsets.zero,
      onPressed: onPressed,
      child: Icon(icon, size: size, color: color),
    );
  }
}
