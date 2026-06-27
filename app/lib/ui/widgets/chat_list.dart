import 'package:app/model/chat_room.dart';
import 'package:app/ui/widgets/chat_room_tile.dart';
import 'package:flutter/cupertino.dart';

class ChatList extends StatelessWidget {
  const ChatList({
    super.key,
    required this.rooms,
    this.onRefresh,
    this.onTapRoom,
  });

  final List<ChatRoom> rooms;

  /// 당겨서 새로고침 콜백. null이면 새로고침을 비활성화한다.
  final Future<void> Function()? onRefresh;
  final void Function(ChatRoom room)? onTapRoom;

  @override
  Widget build(BuildContext context) {
    return CupertinoScrollbar(
      child: CustomScrollView(
        slivers: [
          if (onRefresh != null)
            CupertinoSliverRefreshControl(onRefresh: onRefresh),
          SliverPadding(
            // 하단 탭바(높이 60 + 안전영역) 뒤로 콘텐츠가 스크롤되도록 여백 확보.
            padding: EdgeInsets.only(
              bottom: 12 + 60 + MediaQuery.of(context).padding.bottom,
            ),
            sliver: SliverList.builder(
              itemCount: rooms.length,
              itemBuilder: (context, index) {
                final room = rooms[index];
                return ChatRoomTile(
                  room: room,
                  onTap: () => onTapRoom?.call(room),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
