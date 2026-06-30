import 'package:flutter/material.dart';

/// 앱 공통 디자인 토큰 (모서리 반경).
class AppRadius {
  AppRadius._();

  /// 입력창·버튼 등 공통 모서리 반경.
  static const double field = 12;

  static final BorderRadius fieldBorderRadius = BorderRadius.circular(field);
}

/// 입력창·버튼 등에 공통으로 쓰이는 회색 채움색.
///
/// 위젯에서는 보통 `Theme.of(context).inputDecorationTheme.fillColor`로
/// 현재 모드에 맞는 값을 읽어 쓴다.
class AppColors {
  AppColors._();

  static final Color fieldLight = Colors.grey.shade200;
  static final Color fieldDark = Colors.grey.shade900;
}

/// 앱 전역 테마.
class AppTheme {
  AppTheme._();

  static ThemeData get light => ThemeData(
    colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
    scaffoldBackgroundColor: Colors.white,
    appBarTheme: _appBarTheme(
      background: Colors.white,
      foreground: Colors.black,
    ),
    inputDecorationTheme: _inputDecorationTheme(AppColors.fieldLight),
  );

  static ThemeData get dark => ThemeData(
    colorScheme: ColorScheme.fromSeed(
      seedColor: Colors.blue,
      brightness: Brightness.dark,
    ),
    scaffoldBackgroundColor: Colors.black,
    appBarTheme: _appBarTheme(
      background: Colors.black,
      foreground: Colors.white,
    ),
    inputDecorationTheme: _inputDecorationTheme(AppColors.fieldDark),
  );

  /// 모드별 배경/전경색을 적용한 공통 AppBar 스타일.
  static AppBarTheme _appBarTheme({
    required Color background,
    required Color foreground,
  }) {
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
  static InputDecorationTheme _inputDecorationTheme(Color fillColor) {
    return InputDecorationTheme(
      filled: true,
      fillColor: fillColor,
      contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      border: OutlineInputBorder(
        borderRadius: AppRadius.fieldBorderRadius,
        borderSide: BorderSide.none,
      ),
    );
  }
}
