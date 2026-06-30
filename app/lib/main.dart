import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import 'ui/login/login_view.dart';

void main() {
  runApp(const ProviderScope(child: MyApp()));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // 빈 여백을 탭하면 키보드를 내린다. (모든 화면에 적용)
      builder: (context, child) {
        return GestureDetector(
          behavior: HitTestBehavior.translucent,
          onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
          child: child,
        );
      },
      // 시스템 설정(라이트/다크)에 따라 테마가 자동 전환된다.
      themeMode: ThemeMode.system,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        scaffoldBackgroundColor: Colors.white,
        appBarTheme: _appBarTheme(background: Colors.white, foreground: Colors.black),
        inputDecorationTheme: _inputDecorationTheme(Colors.grey.shade200),
      ),
      darkTheme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.blue,
          brightness: Brightness.dark,
        ),
        scaffoldBackgroundColor: Colors.black,
        appBarTheme: _appBarTheme(background: Colors.black, foreground: Colors.white),
        inputDecorationTheme: _inputDecorationTheme(Colors.grey.shade900),
      ),
      home: const LoginView(),
    );
  }
}

/// 모드별 배경/전경색을 적용한 공통 AppBar 스타일.
AppBarTheme _appBarTheme({required Color background, required Color foreground}) {
  return AppBarTheme(
    backgroundColor: background,
    foregroundColor: foreground,
    titleTextStyle: TextStyle(
      color: foreground,
      fontSize: 16,
      fontWeight: FontWeight.w600,
    ),
    // Material3의 스크롤 시 색 틴팅 제거 → 배경색을 그대로 유지.
    surfaceTintColor: Colors.transparent,
    scrolledUnderElevation: 0,
    elevation: 0,
  );
}

/// 둥근 모서리 + 회색 배경의 공통 입력창 스타일.
InputDecorationTheme _inputDecorationTheme(Color fillColor) {
  return InputDecorationTheme(
    filled: true,
    fillColor: fillColor,
    contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
    border: OutlineInputBorder(
      borderRadius: BorderRadius.circular(12),
      borderSide: BorderSide.none,
    ),
  );
}
