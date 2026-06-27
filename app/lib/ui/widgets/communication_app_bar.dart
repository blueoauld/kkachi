import 'package:app/ui/widgets/nav_icon_button.dart';
import 'package:flutter/cupertino.dart';

class CommunicationAppBar extends StatelessWidget
    implements ObstructingPreferredSizeWidget {
  const CommunicationAppBar({
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
      middle: const Text('소통'),
      leading: NavIconButton(icon: CupertinoIcons.search, onPressed: onSearch),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          NavIconButton(
            icon: CupertinoIcons.slider_horizontal_3,
            onPressed: onFilter,
          ),
          NavIconButton(
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
