import 'package:app/model/member.dart';
import 'package:app/model/member_detail.dart';
import 'package:app/ui/view/photo_gallery_view.dart';
import 'package:app/ui/widgets/app_icon_button.dart';
import 'package:app/ui/widgets/member_detail_action_bar.dart';
import 'package:app/ui/widgets/member_detail_menu_sheet.dart';
import 'package:app/ui/widgets/message_compose_sheet.dart';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';

const _mockMemberDetail = MemberDetail(
  publicImageUrls: [
    'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800',
    'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=800',
    'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=800',
    'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800',
  ],
  privateImageCount: 0,
  nickname: '홍길동',
  updatedAt: '방금전',
  gender: '여자',
  age: 27,
  distance: 1.2,
  bio:
      '안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 :) 안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 : 안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 : 안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 : 안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 :  안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 : 안녕하세요! 사진 찍는 거 좋아하고 주말엔 등산 다녀요. 편하게 대화해요 :',
  hearts: 128,
);

class MemberDetailView extends StatefulWidget {
  const MemberDetailView({super.key, required this.member});

  final Member member;

  @override
  State<MemberDetailView> createState() => _MemberDetailViewState();
}

class _MemberDetailViewState extends State<MemberDetailView> {
  final MemberDetail _detail = _mockMemberDetail;
  final PageController _pageController = PageController();
  int _currentPage = 0;

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  Future<void> _handleBlock(BuildContext context) async {
    final confirmed = await showCupertinoDialog<bool>(
      context: context,
      builder: (context) => CupertinoAlertDialog(
        title: Text('${_detail.nickname}님을 차단할까요?'),
        content: const Padding(
          padding: EdgeInsets.only(top: 8),
          child: Text('차단하면 대화 내역이 모두 삭제되며, 서로의 목록에서도 표시되지 않습니다.'),
        ),
        actions: [
          CupertinoDialogAction(
            onPressed: () => Navigator.pop(context, false),
            child: const Text('닫기'),
          ),
          CupertinoDialogAction(
            isDestructiveAction: true,
            onPressed: () => Navigator.pop(context, true),
            child: const Text('차단'),
          ),
        ],
      ),
    );

    if (confirmed == true && context.mounted) {
      Navigator.of(context).pop();
    }
  }

  Future<void> _handleMessage(BuildContext context) async {
    await MessageComposeSheet.show(context);
  }

  Widget _buildImageTabView(BuildContext context) {
    final images = _detail.publicImageUrls;

    Widget buildFill(Widget child) => Container(
      color: CupertinoColors.systemGrey5.resolveFrom(context),
      alignment: Alignment.center,
      child: child,
    );

    final placeholder = buildFill(
      Icon(
        CupertinoIcons.person_fill,
        size: 96,
        color: CupertinoColors.systemGrey.resolveFrom(context),
      ),
    );

    final loading = buildFill(const CupertinoActivityIndicator(radius: 16));

    final error = buildFill(
      Icon(
        CupertinoIcons.xmark,
        size: 64,
        color: CupertinoColors.systemGrey.resolveFrom(context),
      ),
    );

    return AspectRatio(
      aspectRatio: 1,
      child: Stack(
        children: [
          if (images.isEmpty)
            placeholder
          else
            PageView.builder(
              controller: _pageController,
              itemCount: images.length,
              onPageChanged: (index) => setState(() => _currentPage = index),
              itemBuilder: (context, index) {
                return GestureDetector(
                  onTap: () => PhotoGalleryView.show(
                    context,
                    images: images,
                    initialIndex: index,
                  ),
                  child: CachedNetworkImage(
                    imageUrl: images[index],
                    fit: BoxFit.cover,
                    placeholder: (_, _) => loading,
                    errorWidget: (_, _, _) => error,
                  ),
                );
              },
            ),
          if (images.length > 1)
            Positioned(
              bottom: 12,
              left: 0,
              right: 0,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(images.length, (index) {
                  final isActive = index == _currentPage;
                  return Container(
                    width: 8,
                    height: 8,
                    margin: const EdgeInsets.symmetric(horizontal: 3),
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: isActive
                          ? CupertinoColors.white
                          : CupertinoColors.white.withValues(alpha: 0.4),
                    ),
                  );
                }),
              ),
            ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final secondaryColor = CupertinoDynamicColor.resolve(
      CupertinoColors.secondaryLabel,
      context,
    );

    return CupertinoPageScaffold(
      resizeToAvoidBottomInset: false,
      navigationBar: CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: const Text('프로필'),
        trailing: AppIconButton(
          icon: CupertinoIcons.ellipsis,
          onPressed: () =>
              MemberDetailMenuSheet.show(context, nickname: _detail.nickname),
        ),
      ),
      child: Column(
        children: [
          Expanded(
            child: SafeArea(
              bottom: false,
              child: SingleChildScrollView(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _buildImageTabView(context),
                    Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            textBaseline: TextBaseline.alphabetic,
                            children: [
                              Expanded(
                                child: Text(
                                  _detail.nickname,
                                  style: const TextStyle(
                                    fontSize: 22,
                                    fontWeight: FontWeight.w700,
                                  ),
                                ),
                              ),
                              const SizedBox(width: 8),
                              Text(
                                _detail.updatedAt,
                                style: TextStyle(
                                  fontSize: 13,
                                  color: secondaryColor,
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 6),
                          Row(
                            children: [
                              Text(
                                '${_detail.gender} · ${_detail.age}살 · ',
                                style: TextStyle(
                                  fontSize: 15,
                                  color: secondaryColor,
                                ),
                              ),
                              Icon(
                                CupertinoIcons.heart_fill,
                                size: 15,
                                color: CupertinoColors.systemRed.resolveFrom(
                                  context,
                                ),
                              ),
                              const SizedBox(width: 4),
                              Text(
                                '${_detail.hearts}',
                                style: TextStyle(
                                  fontSize: 15,
                                  color: secondaryColor,
                                ),
                              ),
                              const Spacer(),
                              if (_detail.distance != null)
                                Text(
                                  '${_detail.distance!.toStringAsFixed(1)}km',
                                  style: TextStyle(
                                    fontSize: 13,
                                    color: secondaryColor,
                                  ),
                                ),
                            ],
                          ),
                          const SizedBox(height: 16),
                          Container(
                            width: double.infinity,
                            padding: const EdgeInsets.all(16),
                            decoration: BoxDecoration(
                              color: CupertinoColors.systemGrey6.resolveFrom(
                                context,
                              ),
                              borderRadius: BorderRadius.circular(12),
                            ),
                            child: Text(
                              _detail.bio,
                              style: const TextStyle(fontSize: 16, height: 1.4),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          MemberDetailActionBar(
            privateImageCount: _detail.privateImageCount,
            onMessage: () => _handleMessage(context),
            onBlock: () => _handleBlock(context),
          ),
        ],
      ),
    );
  }
}
