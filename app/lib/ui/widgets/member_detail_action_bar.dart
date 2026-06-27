import 'package:app/ui/widgets/app_icon_button.dart';
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
    final iconColor = CupertinoColors.systemGrey.resolveFrom(context);

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
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              AppIconButton(
                icon: CupertinoIcons.star_fill,
                color: iconColor,
                onPressed: onFavorite ?? () {},
              ),
              AppIconButton(
                icon: CupertinoIcons.heart_fill,
                color: iconColor,
                onPressed: onLike ?? () {},
              ),
              AppIconButton(
                icon: CupertinoIcons.bubble_left_fill,
                color: iconColor,
                onPressed: onMessage ?? () {},
              ),
              Stack(
                clipBehavior: Clip.none,
                children: [
                  AppIconButton(
                    icon: CupertinoIcons.photo_fill,
                    color: iconColor,
                    onPressed: onPhoto ?? () {},
                  ),
                  Positioned(
                    top: 2,
                    right: -2,
                    child: _CountBadge(count: privateImageCount),
                  ),
                ],
              ),
              AppIconButton(
                icon: CupertinoIcons.nosign,
                color: iconColor,
                onPressed: onBlock ?? () {},
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
      constraints: const BoxConstraints(minWidth: 18, minHeight: 18),
      padding: const EdgeInsets.symmetric(horizontal: 5),
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: count > 0
            ? CupertinoColors.systemRed.resolveFrom(context)
            : CupertinoColors.systemGrey2.resolveFrom(context),
        borderRadius: BorderRadius.circular(9),
      ),
      child: Text(
        '$count',
        textAlign: TextAlign.center,
        style: const TextStyle(
          color: CupertinoColors.white,
          fontSize: 11,
          fontWeight: FontWeight.w600,
          height: 1,
        ),
      ),
    );
  }
}
