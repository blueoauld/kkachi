import 'package:app/model/member.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';

class MemberListTile extends StatelessWidget {
  const MemberListTile({super.key, required this.member, this.onTap});

  final Member member;
  final VoidCallback? onTap;

  Widget _buildProfile(BuildContext context) {
    const size = 64.0;
    final url = member.profileUrl;

    Widget buildFill(Widget child) => Container(
      width: size,
      height: size,
      alignment: Alignment.center,
      color: CupertinoColors.systemGrey5.resolveFrom(context),
      child: child,
    );

    final placeholder = buildFill(
      Icon(
        CupertinoIcons.person_fill,
        size: 32,
        color: CupertinoColors.systemGrey.resolveFrom(context),
      ),
    );

    final loading = buildFill(const CupertinoActivityIndicator(radius: 10));

    final error = buildFill(
      Icon(
        CupertinoIcons.xmark,
        size: 24,
        color: CupertinoColors.systemGrey.resolveFrom(context),
      ),
    );

    return ClipRRect(
      borderRadius: BorderRadius.circular(12),
      child: (url == null || url.isEmpty)
          ? placeholder
          : CachedNetworkImage(
              imageUrl: url,
              width: size,
              height: size,
              fit: BoxFit.cover,
              placeholder: (_, _) => loading,
              errorWidget: (_, _, _) => error,
            ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final secondaryColor = CupertinoDynamicColor.resolve(
      CupertinoColors.secondaryLabel,
      context,
    );

    return CupertinoButton(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
      onPressed: onTap,
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          _buildProfile(context),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Expanded(
                      child: Text(
                        member.nickname,
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                    Text(
                      member.updatedAt,
                      style: TextStyle(fontSize: 11, color: secondaryColor),
                    ),
                  ],
                ),
                const SizedBox(height: 2),
                Row(
                  children: [
                    Text(
                      '${member.gender} · ${member.age}살 · ',
                      style: TextStyle(fontSize: 13, color: secondaryColor),
                    ),
                    Icon(
                      CupertinoIcons.heart_fill,
                      size: 14,
                      color: CupertinoColors.systemRed.resolveFrom(context),
                    ),
                    const SizedBox(width: 4),
                    Text(
                      '${member.hearts}',
                      style: TextStyle(fontSize: 13, color: secondaryColor),
                    ),
                  ],
                ),
                const SizedBox(height: 2),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Expanded(
                      child: Text(
                        member.comment,
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(fontSize: 13, color: secondaryColor),
                      ),
                    ),
                    if (member.distance != null) ...[
                      const SizedBox(width: 8),
                      Text(
                        '${member.distance!.toStringAsFixed(1)}km',
                        style: TextStyle(fontSize: 11, color: secondaryColor),
                      ),
                    ],
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
