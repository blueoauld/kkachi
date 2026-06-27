import 'package:cached_network_image/cached_network_image.dart';
import 'package:dismissible_page/dismissible_page.dart';
import 'package:flutter/cupertino.dart';
import 'package:photo_view/photo_view.dart';
import 'package:photo_view/photo_view_gallery.dart';

/// 더블탭 시 줌인(covered)과 원래 크기(initial)를 토글한다.
PhotoViewScaleState _scaleStateCycle(PhotoViewScaleState actual) {
  return actual == PhotoViewScaleState.initial
      ? PhotoViewScaleState.covering
      : PhotoViewScaleState.initial;
}

/// 사진을 전체화면으로 확대/스와이프해 볼 수 있는 뷰어.
class PhotoGalleryView extends StatefulWidget {
  const PhotoGalleryView({
    super.key,
    required this.images,
    this.initialIndex = 0,
  });

  final List<String> images;
  final int initialIndex;

  /// 전체화면 모달로 뷰어를 띄운다. 위/아래로 당기면 닫힌다.
  static Future<void> show(
    BuildContext context, {
    required List<String> images,
    int initialIndex = 0,
  }) {
    return Navigator.of(context, rootNavigator: true).push(
      PageRouteBuilder(
        opaque: false,
        barrierColor: CupertinoColors.transparent,
        pageBuilder: (_, animation, _) => FadeTransition(
          opacity: animation,
          child: PhotoGalleryView(images: images, initialIndex: initialIndex),
        ),
      ),
    );
  }

  @override
  State<PhotoGalleryView> createState() => _PhotoGalleryViewState();
}

class _PhotoGalleryViewState extends State<PhotoGalleryView> {
  late final PageController _pageController = PageController(
    initialPage: widget.initialIndex,
  );
  late int _currentPage = widget.initialIndex;

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return DismissiblePage(
      onDismissed: () => Navigator.of(context).pop(),
      direction: DismissiblePageDismissDirection.down,
      backgroundColor: CupertinoColors.black,
      child: Stack(
        children: [
          PhotoViewGallery.builder(
            pageController: _pageController,
            itemCount: widget.images.length,
            onPageChanged: (index) => setState(() => _currentPage = index),
            backgroundDecoration: const BoxDecoration(
              color: CupertinoColors.transparent,
            ),
            loadingBuilder: (_, _) =>
                const Center(child: CupertinoActivityIndicator(radius: 16)),
            builder: (context, index) {
              return PhotoViewGalleryPageOptions(
                imageProvider: CachedNetworkImageProvider(widget.images[index]),
                minScale: PhotoViewComputedScale.contained,
                maxScale: PhotoViewComputedScale.covered * 3,
                scaleStateCycle: _scaleStateCycle,
                errorBuilder: (_, _, _) => const Center(
                  child: Icon(
                    CupertinoIcons.xmark,
                    size: 64,
                    color: CupertinoColors.systemGrey,
                  ),
                ),
              );
            },
          ),
          SafeArea(
            child: Padding(
              padding: const EdgeInsets.all(8),
              child: Align(
                alignment: Alignment.topLeft,
                child: CupertinoButton(
                  padding: const EdgeInsets.all(8),
                  minimumSize: Size.zero,
                  onPressed: () => Navigator.of(context).pop(),
                  child: const Icon(
                    CupertinoIcons.xmark,
                    color: CupertinoColors.white,
                    size: 28,
                  ),
                ),
              ),
            ),
          ),
          if (widget.images.length > 1)
            Positioned(
              bottom: 0,
              left: 0,
              right: 0,
              child: SafeArea(
                child: Padding(
                  padding: const EdgeInsets.only(bottom: 16),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: List.generate(widget.images.length, (index) {
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
              ),
            ),
        ],
      ),
    );
  }
}
