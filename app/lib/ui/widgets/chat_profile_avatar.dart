import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';

/// 채팅 헤더의 둥근 정사각형 프로필.
class ChatProfileAvatar extends StatelessWidget {
  const ChatProfileAvatar({super.key, required this.url, this.onTap});

  final String? url;
  final VoidCallback? onTap;

  /// 이미지 한 변 길이.
  static const double _size = 35;

  @override
  Widget build(BuildContext context) {
    Widget buildFill(Widget child) => Container(
      width: _size,
      height: _size,
      alignment: Alignment.center,
      color: CupertinoColors.systemGrey5.resolveFrom(context),
      child: child,
    );

    final placeholder = buildFill(
      Icon(
        CupertinoIcons.person_fill,
        size: 16,
        color: CupertinoColors.systemGrey.resolveFrom(context),
      ),
    );

    return GestureDetector(
      onTap: onTap,
      child: Container(
        // 둥근 정사각형 + 회색 테두리.
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(10),
          border: Border.all(
            color: CupertinoColors.systemGrey4.resolveFrom(context),
            width: 1,
          ),
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(10),
          child: (url == null || url!.isEmpty)
              ? placeholder
              : CachedNetworkImage(
                  imageUrl: url!,
                  width: _size,
                  height: _size,
                  fit: BoxFit.cover,
                  placeholder: (_, _) =>
                      buildFill(const CupertinoActivityIndicator(radius: 8)),
                  errorWidget: (_, _, _) => placeholder,
                ),
        ),
      ),
    );
  }
}
