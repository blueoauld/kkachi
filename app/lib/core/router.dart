import 'package:go_router/go_router.dart';

import '../ui/login/login_view.dart';
import '../ui/signup/signup_view.dart';

/// 앱 전역 라우터.
final appRouter = GoRouter(
  routes: [
    GoRoute(path: '/', builder: (context, state) => const LoginView()),
    GoRoute(path: '/signup', builder: (context, state) => const SignUpView()),
  ],
);
