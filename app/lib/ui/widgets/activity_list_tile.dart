import 'package:app/model/member_activity.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';

/// 좋아요/비밀 사진/차단 목록 등 활동 목록의 행 한 개.
///
/// [MemberListTile]을 참고하되, 이 화면에서는 거리·갱신 시각·하트 수를
/// 표시하지 않고 프로필·닉네임·성별/나이·멘트만 보여준다.
class ActivityListTile extends StatelessWidget {
  const ActivityListTile({
    super.key,
    required this.member,
    this.onTap,
    this.onDelete,
  });

  final MemberActivity member;
  final VoidCallback? onTap;

  /// 행 오른쪽 휴지통 버튼을 눌렀을 때의 콜백. (목록에서 삭제)
  final VoidCallback? onDelete;

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
                Text(
                  member.nickname,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w600,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  '${member.gender} · ${member.age}살',
                  style: TextStyle(fontSize: 13, color: secondaryColor),
                ),
                const SizedBox(height: 2),
                Text(
                  member.comment,
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(fontSize: 13, color: secondaryColor),
                ),
              ],
            ),
          ),
          const SizedBox(width: 12),
          _buildDeleteButton(context),
        ],
      ),
    );
  }

  Widget _buildDeleteButton(BuildContext context) {
    return CupertinoButton(
      padding: EdgeInsets.zero,
      minimumSize: Size.zero,
      onPressed: onDelete,
      child: Container(
        width: 40,
        height: 40,
        decoration: BoxDecoration(
          color: CupertinoColors.systemRed.resolveFrom(context),
          shape: BoxShape.circle,
        ),
        alignment: Alignment.center,
        child: const Icon(
          CupertinoIcons.trash_fill,
          size: 22,
          color: CupertinoColors.white,
        ),
      ),
    );
  }
}
