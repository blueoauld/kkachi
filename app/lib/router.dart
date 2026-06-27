import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

import 'model/member.dart';
import 'ui/home/home_page.dart';
import 'ui/view/chat_view.dart';
import 'ui/view/main_view.dart';
import 'ui/view/member_detail_view.dart';
import 'ui/view/profile_view.dart';
import 'ui/view/ranking_view.dart';

/// 앱 라우트 경로 모음.
abstract final class AppRoutes {
  static const main = '/main';
  static const chat = '/chat';
  static const ranking = '/ranking';
  static const profile = '/profile';
  static const member = 'member';
}

final GlobalKey<NavigatorState> _rootNavigatorKey = GlobalKey<NavigatorState>(
  debugLabel: 'root',
);

final GoRouter appRouter = GoRouter(
  navigatorKey: _rootNavigatorKey,
  initialLocation: AppRoutes.main,
  routes: [
    StatefulShellRoute.indexedStack(
      builder: (context, state, navigationShell) =>
          HomePage(navigationShell: navigationShell),
      branches: [
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.main,
              builder: (context, state) => const MainView(),
              routes: [
                GoRoute(
                  path: AppRoutes.member,
                  parentNavigatorKey: _rootNavigatorKey,
                  builder: (context, state) =>
                      MemberDetailView(member: state.extra! as Member),
                ),
              ],
            ),
          ],
        ),
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.chat,
              builder: (context, state) => const ChatView(),
            ),
          ],
        ),
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.ranking,
              builder: (context, state) => const RankingView(),
            ),
          ],
        ),
        StatefulShellBranch(
          routes: [
            GoRoute(
              path: AppRoutes.profile,
              builder: (context, state) => const ProfileView(),
            ),
          ],
        ),
      ],
    ),
  ],
);
