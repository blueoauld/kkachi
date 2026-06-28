import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart' show DefaultMaterialLocalizations;

import 'router.dart';

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return CupertinoApp.router(
      title: '투투',
      theme: const CupertinoThemeData(primaryColor: CupertinoColors.label),
      // Android용 Material 위젯(바텀시트 등)이 필요로 하는 로컬라이제이션.
      localizationsDelegates: const [DefaultMaterialLocalizations.delegate],
      routerConfig: appRouter,
    );
  }
}
