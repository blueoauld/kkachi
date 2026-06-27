import 'package:flutter/cupertino.dart';

const _kTabBarBorderColor = CupertinoDynamicColor.withBrightness(
  color: Color(0x4C000000),
  darkColor: Color(0x29000000),
);

class MemberDetailActionBar extends StatelessWidget {
  const MemberDetailActionBar({
    super.key,
    this.privateImageCount = 0,
    this.onFavorite,
    this.onLike,
    this.onMessage,
    this.onPhoto,
    this.onBlock,
  });

  final int privateImageCount;
  final VoidCallback? onFavorite;
  final VoidCallback? onLike;
  final VoidCallback? onMessage;
  final VoidCallback? onPhoto;
  final VoidCallback? onBlock;

  @override
  Widget build(BuildContext context) {
    final color = CupertinoColors.systemGrey.resolveFrom(context);

    return DecoratedBox(
      decoration: BoxDecoration(
        color: CupertinoTheme.of(context).barBackgroundColor,
        border: Border(
          top: BorderSide(
            color: _kTabBarBorderColor.resolveFrom(context),
            width: 0,
          ),
        ),
      ),
      child: SafeArea(
        top: false,
        child: SizedBox(
          height: 60,
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              _ActionItem(
                icon: CupertinoIcons.star_fill,
                label: '즐겨찾기',
                color: color,
                onPressed: onFavorite,
              ),
              _ActionItem(
                icon: CupertinoIcons.heart_fill,
                label: '좋아요',
                color: color,
                onPressed: onLike,
              ),
              _ActionItem(
                icon: CupertinoIcons.bubble_left_fill,
                label: '쪽지',
                color: color,
                onPressed: onMessage,
              ),
              _ActionItem(
                icon: CupertinoIcons.photo_fill,
                label: '비밀 사진',
                color: color,
                onPressed: onPhoto,
                badge: _CountBadge(count: privateImageCount),
              ),
              _ActionItem(
                icon: CupertinoIcons.nosign,
                label: '차단',
                color: color,
                onPressed: onBlock,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _ActionItem extends StatelessWidget {
  const _ActionItem({
    required this.icon,
    required this.label,
    required this.color,
    this.onPressed,
    this.badge,
  });

  final IconData icon;
  final String label;
  final Color color;
  final VoidCallback? onPressed;
  final Widget? badge;

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: CupertinoButton(
        padding: EdgeInsets.zero,
        minimumSize: Size.zero,
        onPressed: onPressed ?? () {},
        child: Padding(
          padding: const EdgeInsets.only(bottom: 4),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              Expanded(
                child: Center(
                  child: Padding(
                    padding: const EdgeInsets.only(top: 6),
                    child: Stack(
                      clipBehavior: Clip.none,
                      children: [
                        Icon(icon, size: 24, color: color),
                        if (badge != null)
                          Positioned(top: -4, right: -8, child: badge!),
                      ],
                    ),
                  ),
                ),
              ),
              Text(
                label,
                style: TextStyle(fontSize: 10, color: color, height: 1),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _CountBadge extends StatelessWidget {
  const _CountBadge({required this.count});

  final int count;

  @override
  Widget build(BuildContext context) {
    return Container(
      constraints: const BoxConstraints(minWidth: 16, minHeight: 16),
      padding: const EdgeInsets.symmetric(horizontal: 4),
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: count > 0
            ? CupertinoColors.systemRed.resolveFrom(context)
            : CupertinoColors.systemGrey2.resolveFrom(context),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Text(
        '$count',
        textAlign: TextAlign.center,
        style: const TextStyle(
          color: CupertinoColors.white,
          fontSize: 10,
          fontWeight: FontWeight.w600,
          height: 1,
        ),
      ),
    );
  }
}
