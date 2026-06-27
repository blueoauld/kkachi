import 'package:flutter/cupertino.dart';

class NavIconButton extends StatelessWidget {
  const NavIconButton({
    super.key,
    required this.icon,
    required this.onPressed,
    this.size = 28,
  });

  final IconData icon;
  final VoidCallback onPressed;
  final double size;

  @override
  Widget build(BuildContext context) {
    final color = CupertinoDynamicColor.resolve(CupertinoColors.label, context);
    return CupertinoButton(
      padding: EdgeInsets.zero,
      onPressed: onPressed,
      child: Icon(icon, color: color, size: size),
    );
  }
}
