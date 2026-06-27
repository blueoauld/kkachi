import 'package:app/model/chat_room.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';

class ChatRoomTile extends StatelessWidget {
  const ChatRoomTile({super.key, required this.room, this.onTap});

  final ChatRoom room;
  final VoidCallback? onTap;

  Widget _buildProfile(BuildContext context) {
    const size = 64.0;
    final url = room.profileUrl;

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
                        room.nickname,
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    ),
                    Text(
                      room.updatedAt,
                      style: TextStyle(fontSize: 11, color: secondaryColor),
                    ),
                  ],
                ),
                const SizedBox(height: 4),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Expanded(
                      child: Text(
                        room.lastMessage,
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(fontSize: 13, color: secondaryColor),
                      ),
                    ),
                    if (room.hasUnread) ...[
                      const SizedBox(width: 8),
                      _UnreadBadge(count: room.unreadCount),
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

class _UnreadBadge extends StatelessWidget {
  const _UnreadBadge({required this.count});

  final int count;

  @override
  Widget build(BuildContext context) {
    return Container(
      constraints: const BoxConstraints(minWidth: 20),
      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
      decoration: BoxDecoration(
        color: CupertinoColors.systemRed.resolveFrom(context),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Text(
        count > 99 ? '99+' : '$count',
        textAlign: TextAlign.center,
        style: const TextStyle(
          fontSize: 11,
          fontWeight: FontWeight.w600,
          color: CupertinoColors.white,
        ),
      ),
    );
  }
}
