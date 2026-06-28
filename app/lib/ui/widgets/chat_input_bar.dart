import 'dart:io' show Platform;

import 'package:flutter/cupertino.dart';

/// 하단 입력 바. 길쭉한 캡슐 안에 왼쪽 + 버튼, 오른쪽 전송 버튼을 둔다.
class ChatInputBar extends StatelessWidget {
  const ChatInputBar({
    super.key,
    required this.controller,
    required this.onPlus,
    required this.onSend,
  });

  /// + / 전송 버튼의 원 지름.
  static const double _buttonSize = 36;

  final TextEditingController controller;
  final VoidCallback onPlus;
  final VoidCallback onSend;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12, 8, 12, 8),
      child: Container(
        // 캡슐 형태의 길쭉한 입력창. 안에 버튼과 텍스트필드가 들어간다.
        padding: const EdgeInsets.all(4),
        decoration: BoxDecoration(
          color: CupertinoColors.systemGrey6.resolveFrom(context),
          borderRadius: BorderRadius.circular(24),
        ),
        child: Row(
          // 버튼은 하단 고정. 입력이 여러 줄로 늘어나도 + / 전송은 맨 아래에 머문다.
          crossAxisAlignment: CrossAxisAlignment.end,
          children: [
            // 왼쪽 + 버튼 (입력창과 구분되는 회색 원).
            CupertinoButton(
              padding: EdgeInsets.zero,
              minimumSize: const Size(_buttonSize, _buttonSize),
              onPressed: onPlus,
              child: Container(
                width: _buttonSize,
                height: _buttonSize,
                alignment: Alignment.center,
                decoration: BoxDecoration(
                  color: CupertinoColors.systemGrey4.resolveFrom(context),
                  shape: BoxShape.circle,
                ),
                child: Icon(
                  CupertinoIcons.plus,
                  size: 22,
                  color: CupertinoColors.label.resolveFrom(context),
                ),
              ),
            ),
            const SizedBox(width: 8),
            // 가운데 입력 필드.
            Expanded(
              child: ConstrainedBox(
                constraints: const BoxConstraints(minHeight: _buttonSize),
                child: CupertinoTextField(
                  controller: controller,
                  minLines: 1,
                  maxLines: 5,
                  autocorrect: false,
                  enableSuggestions: false,
                  placeholder: '메시지 입력',
                  // Android는 폰트 패딩으로 한 줄 높이가 커져 버튼보다 높아진다.
                  // 세로 패딩을 줄여 한 줄 높이를 버튼(36) 이하로 맞춰 상하 여백을 대칭으로 만든다.
                  padding: EdgeInsets.symmetric(
                    vertical: Platform.isAndroid ? 4 : 8,
                  ),
                  decoration: const BoxDecoration(),
                  onSubmitted: (_) => onSend(),
                ),
              ),
            ),
            const SizedBox(width: 4),
            // 오른쪽 전송 버튼.
            CupertinoButton(
              padding: EdgeInsets.zero,
              minimumSize: const Size(_buttonSize, _buttonSize),
              onPressed: onSend,
              child: Container(
                width: _buttonSize,
                height: _buttonSize,
                alignment: Alignment.center,
                decoration: BoxDecoration(
                  color: CupertinoColors.activeBlue.resolveFrom(context),
                  shape: BoxShape.circle,
                ),
                child: const Icon(
                  CupertinoIcons.paperplane_fill,
                  size: 20,
                  color: CupertinoColors.white,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
