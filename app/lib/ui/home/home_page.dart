import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key, required this.navigationShell});

  /// 탭(브랜치) 상태를 관리하는 go_router 셸.
  final StatefulNavigationShell navigationShell;

  void _onTap(int index) {
    navigationShell.goBranch(
      index,
      // 이미 선택된 탭을 다시 누르면 해당 탭의 첫 화면으로 이동한다.
      initialLocation: index == navigationShell.currentIndex,
    );
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      resizeToAvoidBottomInset: false,
      child: Column(
        children: [
          Expanded(child: navigationShell),
          CupertinoTabBar(
            activeColor: CupertinoColors.label,
            height: 60,
            currentIndex: navigationShell.currentIndex,
            onTap: _onTap,
            items: const [
              BottomNavigationBarItem(
                icon: Padding(
                  padding: EdgeInsets.only(top: 6),
                  child: Icon(CupertinoIcons.flame_fill),
                ),
                label: '메인',
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
        ],
      ),
    );
  }
}
