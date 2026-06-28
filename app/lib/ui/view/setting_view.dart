import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../router.dart';
import '../widgets/app_icon_button.dart';
import '../widgets/setting_menu_sheet.dart';

/// 설정 화면의 메뉴 한 항목.
class _SettingItem {
  const _SettingItem({
    required this.icon,
    required this.title,
    required this.color,
    this.launchUri,
    this.launchMode = LaunchMode.externalApplication,
    this.route,
  });

  final IconData icon;
  final String title;
  final Color color;

  /// 항목을 탭했을 때 외부로 연결할 주소. (이메일/웹)
  final Uri? launchUri;

  /// 주소를 여는 방식. (외부 앱 / 앱 내부 브라우저)
  final LaunchMode launchMode;

  /// 항목을 탭했을 때 앱 내부에서 이동할 라우트 경로.
  final String? route;
}

class SettingView extends StatelessWidget {
  const SettingView({super.key});

  Future<void> _launch(Uri uri, LaunchMode mode) async {
    await launchUrl(uri, mode: mode);
  }

  static final List<List<_SettingItem>> _sections = [
    [
      _SettingItem(
        icon: CupertinoIcons.person_crop_circle_fill,
        title: '내 프로필',
        color: CupertinoColors.systemBlue,
      ),
    ],
    [
      _SettingItem(
        icon: CupertinoIcons.heart_fill,
        title: '좋아요 목록',
        color: CupertinoColors.systemPink,
        route: '${AppRoutes.setting}/${AppRoutes.like}',
      ),
      _SettingItem(
        icon: CupertinoIcons.lock_fill,
        title: '비밀 사진 목록',
        color: CupertinoColors.systemGreen,
        route: '${AppRoutes.setting}/${AppRoutes.secret}',
      ),
      _SettingItem(
        icon: CupertinoIcons.nosign,
        title: '차단 목록',
        color: CupertinoColors.systemGrey,
        route: '${AppRoutes.setting}/${AppRoutes.block}',
      ),
    ],
    [
      _SettingItem(
        icon: CupertinoIcons.creditcard_fill,
        title: '포인트 내역',
        color: CupertinoColors.systemIndigo,
        route: '${AppRoutes.setting}/${AppRoutes.point}',
      ),
      _SettingItem(
        icon: CupertinoIcons.checkmark_seal_fill,
        title: '출석 체크',
        color: CupertinoColors.systemMint,
      ),
      _SettingItem(
        icon: CupertinoIcons.gift_fill,
        title: '광고 보상',
        color: CupertinoColors.systemOrange,
      ),
    ],
    [
      _SettingItem(
        icon: CupertinoIcons.chat_bubble_2_fill,
        title: '문의사항',
        color: CupertinoColors.systemTeal,
        launchUri: Uri(scheme: 'mailto', path: 'cs@kkachi.org'),
      ),
      _SettingItem(
        icon: CupertinoIcons.lightbulb_fill,
        title: '버그제보',
        color: CupertinoColors.systemYellow,
        launchUri: Uri(scheme: 'mailto', path: 'cs@kkachi.org'),
      ),
      _SettingItem(
        icon: CupertinoIcons.doc_text_fill,
        title: '서비스 이용약관',
        color: CupertinoColors.systemBlue,
        launchUri: Uri.parse('https://kkachi.org'),
        launchMode: LaunchMode.inAppBrowserView,
      ),
      _SettingItem(
        icon: CupertinoIcons.shield_fill,
        title: '개인정보 취급방침',
        color: CupertinoColors.systemBrown,
        launchUri: Uri.parse('https://kkachi.org'),
        launchMode: LaunchMode.inAppBrowserView,
      ),
    ],
  ];

  @override
  Widget build(BuildContext context) {
    final backgroundColor = CupertinoColors.systemGroupedBackground.resolveFrom(
      context,
    );
    final contentColor = CupertinoColors.secondarySystemGroupedBackground
        .resolveFrom(context);

    return CupertinoPageScaffold(
      backgroundColor: backgroundColor,
      navigationBar: CupertinoNavigationBar(
        backgroundColor: backgroundColor,
        middle: const Text('설정'),
        trailing: AppIconButton(
          icon: CupertinoIcons.ellipsis,
          onPressed: () => SettingMenuSheet.show(context),
        ),
      ),
      child: SafeArea(
        bottom: false,
        child: ListView(
          padding: EdgeInsets.only(
            bottom: 12 + 60 + MediaQuery.of(context).padding.bottom,
          ),
          children: [
            for (final section in _sections)
              CupertinoListSection.insetGrouped(
                backgroundColor: backgroundColor,
                decoration: BoxDecoration(
                  color: contentColor,
                  borderRadius: const BorderRadius.all(Radius.circular(10)),
                ),
                children: [
                  for (final item in section)
                    CupertinoListTile.notched(
                      backgroundColor: contentColor,
                      leading: Container(
                        width: 30,
                        height: 30,
                        decoration: BoxDecoration(
                          color: CupertinoDynamicColor.resolve(
                            item.color,
                            context,
                          ),
                          borderRadius: const BorderRadius.all(
                            Radius.circular(7),
                          ),
                        ),
                        alignment: Alignment.center,
                        child: Icon(
                          item.icon,
                          size: 18,
                          color: CupertinoColors.white,
                        ),
                      ),
                      title: Text(item.title),
                      onTap: () {
                        if (item.launchUri != null) {
                          _launch(item.launchUri!, item.launchMode);
                        } else if (item.route != null) {
                          context.push(item.route!);
                        }
                      },
                    ),
                ],
              ),
          ],
        ),
      ),
    );
  }
}
