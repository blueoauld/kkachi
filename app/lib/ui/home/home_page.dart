import 'package:app/ui/view/chat_view.dart';
import 'package:app/ui/view/communication_view.dart';
import 'package:app/ui/view/profile_view.dart';
import 'package:app/ui/view/ranking_view.dart';
import 'package:flutter/cupertino.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return CupertinoTabScaffold(
      tabBar: CupertinoTabBar(
        activeColor: CupertinoColors.label,
        height: 60,
        items: const [
          BottomNavigationBarItem(
            icon: Padding(
              padding: EdgeInsets.only(top: 6),
              child: Icon(CupertinoIcons.flame_fill),
            ),
            label: '소통',
          ),
          BottomNavigationBarItem(
            icon: Padding(
              padding: EdgeInsets.only(top: 6),
              child: Icon(CupertinoIcons.chat_bubble_2_fill),
            ),
            label: '채팅',
          ),
          BottomNavigationBarItem(
            icon: Padding(
              padding: EdgeInsets.only(top: 6),
              child: Icon(CupertinoIcons.star_fill),
            ),
            label: '랭킹',
          ),
          BottomNavigationBarItem(
            icon: Padding(
              padding: EdgeInsets.only(top: 6),
              child: Icon(CupertinoIcons.person_fill),
            ),
            label: '프로필',
          ),
        ],
      ),
      tabBuilder: (context, index) {
        switch (index) {
          case 0:
            return const CommunicationView();
          case 1:
            return const ChatView();
          case 2:
            return const RankingView();
          default:
            return const ProfileView();
        }
      },
    );
  }
}
