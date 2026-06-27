import 'package:app/ui/widgets/app_icon_button.dart';
import 'package:flutter/cupertino.dart';

class ChatAppBar extends StatelessWidget
    implements ObstructingPreferredSizeWidget {
  const ChatAppBar({
    super.key,
    required this.onSearch,
    required this.onNotification,
  });

  final VoidCallback onSearch;
  final VoidCallback onNotification;

  @override
  Widget build(BuildContext context) {
    return CupertinoNavigationBar(
      backgroundColor: CupertinoColors.systemBackground,
      middle: const Text('채팅'),
      leading: AppIconButton(icon: CupertinoIcons.search, onPressed: onSearch),
      trailing: AppIconButton(
        icon: CupertinoIcons.bell,
        onPressed: onNotification,
      ),
    );
  }

  @override
  Size get preferredSize => const CupertinoNavigationBar().preferredSize;

  @override
  bool shouldFullyObstruct(BuildContext context) =>
      const CupertinoNavigationBar().shouldFullyObstruct(context);
}
