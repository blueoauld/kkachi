import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

import 'model/chat_room.dart';
import 'model/member.dart';
import 'ui/home/home_page.dart';
import 'ui/view/chat_detail_view.dart';
import 'ui/view/chat_search_view.dart';
import 'ui/view/chat_view.dart';
import 'ui/view/main_view.dart';
import 'ui/view/member_detail_view.dart';
import 'ui/view/member_search_view.dart';
import 'ui/view/profile_view.dart';
import 'ui/view/ranking_view.dart';
import 'ui/view/report_view.dart';

/// 앱 라우트 경로 모음.
abstract final class AppRoutes {
  static const main = '/main';
  static const chat = '/chat';
  static const ranking = '/ranking';
  static const profile = '/profile';
  static const member = 'member';
  static const report = 'report';
  static const search = 'search';
  static const room = 'room';
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
                  path: AppRoutes.search,
                  parentNavigatorKey: _rootNavigatorKey,
                  builder: (context, state) => const MemberSearchView(),
                ),
                GoRoute(
                  path: AppRoutes.member,
                  parentNavigatorKey: _rootNavigatorKey,
                  builder: (context, state) =>
                      MemberDetailView(member: state.extra! as Member),
                  routes: [
                    GoRoute(
                      path: AppRoutes.report,
                      parentNavigatorKey: _rootNavigatorKey,
                      builder: (context, state) =>
                          ReportView(nickname: state.extra! as String),
                    ),
                  ],
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
              routes: [
                GoRoute(
                  path: AppRoutes.search,
                  parentNavigatorKey: _rootNavigatorKey,
                  builder: (context, state) => const ChatSearchView(),
                ),
                GoRoute(
                  path: AppRoutes.room,
                  parentNavigatorKey: _rootNavigatorKey,
                  builder: (context, state) =>
                      ChatDetailView(room: state.extra! as ChatRoom),
                ),
              ],
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
