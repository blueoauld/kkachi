import 'package:flutter/cupertino.dart';

import 'router.dart';

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return CupertinoApp.router(
      title: '투투',
      theme: const CupertinoThemeData(primaryColor: CupertinoColors.label),
      routerConfig: appRouter,
    );
  }
}
