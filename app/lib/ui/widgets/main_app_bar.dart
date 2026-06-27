import 'package:app/ui/widgets/app_icon_button.dart';
import 'package:flutter/cupertino.dart';

class MainAppBar extends StatelessWidget
    implements ObstructingPreferredSizeWidget {
  const MainAppBar({
    super.key,
    required this.onSearch,
    required this.onFilter,
    required this.onCompose,
  });

  final VoidCallback onSearch;
  final VoidCallback onFilter;
  final VoidCallback onCompose;

  @override
  Widget build(BuildContext context) {
    return CupertinoNavigationBar(
      backgroundColor: CupertinoColors.systemBackground,
      middle: const Text('메인'),
      leading: AppIconButton(icon: CupertinoIcons.search, onPressed: onSearch),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          AppIconButton(
            icon: CupertinoIcons.slider_horizontal_3,
            onPressed: onFilter,
          ),
          AppIconButton(
            icon: CupertinoIcons.square_pencil,
            onPressed: onCompose,
          ),
        ],
      ),
    );
  }

  @override
  Size get preferredSize => const CupertinoNavigationBar().preferredSize;

  @override
  bool shouldFullyObstruct(BuildContext context) =>
      const CupertinoNavigationBar().shouldFullyObstruct(context);
}
