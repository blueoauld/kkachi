import 'dart:io' show Platform;

import 'package:flutter/cupertino.dart';

/// 플랫폼별로 분기되는 세그먼트 컨트롤.
///
/// iOS는 [CupertinoSlidingSegmentedControl], Android는 thumb가 슬라이드하는
/// 커스텀 컨트롤로 렌더링한다. 다크모드를 모두 지원한다.
class AdaptiveSegmentedControl extends StatelessWidget {
  const AdaptiveSegmentedControl({
    super.key,
    required this.value,
    required this.onChanged,
    required this.labels,
  });

  /// 현재 선택된 키.
  final int value;
  final ValueChanged<int> onChanged;

  /// 키 → 표시 라벨.
  final Map<int, String> labels;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
      child: SizedBox(
        width: double.infinity,
        child: Platform.isAndroid
            ? _AndroidSegmented(
                value: value,
                onChanged: onChanged,
                labels: labels,
              )
            : _IosSegmented(value: value, onChanged: onChanged, labels: labels),
      ),
    );
  }
}

class _IosSegmented extends StatelessWidget {
  const _IosSegmented({
    required this.value,
    required this.onChanged,
    required this.labels,
  });

  final int value;
  final ValueChanged<int> onChanged;
  final Map<int, String> labels;

  @override
  Widget build(BuildContext context) {
    return CupertinoSlidingSegmentedControl<int>(
      groupValue: value,
      onValueChanged: (v) {
        if (v != null) onChanged(v);
      },
      children: {
        for (final entry in labels.entries)
          entry.key: Padding(
            padding: const EdgeInsets.symmetric(vertical: 6),
            child: Text(entry.value),
          ),
      },
    );
  }
}

class _AndroidSegmented extends StatelessWidget {
  const _AndroidSegmented({
    required this.value,
    required this.onChanged,
    required this.labels,
  });

  final int value;
  final ValueChanged<int> onChanged;
  final Map<int, String> labels;

  // 다크모드 대응 색상.
  static const Color _trackColor = CupertinoDynamicColor.withBrightness(
    color: Color(0xFFEEEEEE),
    darkColor: Color(0xFF2C2C2E),
  );
  static const Color _thumbColor = CupertinoDynamicColor.withBrightness(
    color: CupertinoColors.white,
    darkColor: Color(0xFF636366),
  );

  @override
  Widget build(BuildContext context) {
    final entries = labels.entries.toList();
    final count = entries.length;
    final selectedIndex = entries.indexWhere((e) => e.key == value);

    final trackColor = CupertinoDynamicColor.resolve(_trackColor, context);
    final thumbColor = CupertinoDynamicColor.resolve(_thumbColor, context);
    final textColor = CupertinoColors.label.resolveFrom(context);

    const padding = 2.0;
    const height = 36.0;

    // iOS 슬라이딩 세그먼트처럼 thumb 하나가 슬라이드한다.
    return LayoutBuilder(
      builder: (context, constraints) {
        final segmentWidth = (constraints.maxWidth - padding * 2) / count;
        return Container(
          height: height,
          padding: const EdgeInsets.all(padding),
          decoration: BoxDecoration(
            color: trackColor,
            borderRadius: BorderRadius.circular(8),
          ),
          child: Stack(
            children: [
              if (selectedIndex >= 0)
                AnimatedPositioned(
                  duration: const Duration(milliseconds: 200),
                  curve: Curves.easeInOut,
                  left: selectedIndex * segmentWidth,
                  top: 0,
                  bottom: 0,
                  width: segmentWidth,
                  child: DecoratedBox(
                    decoration: BoxDecoration(
                      color: thumbColor,
                      borderRadius: BorderRadius.circular(6),
                      boxShadow: [
                        BoxShadow(
                          color: CupertinoColors.black.withValues(alpha: 0.12),
                          blurRadius: 2,
                          offset: const Offset(0, 1),
                        ),
                      ],
                    ),
                  ),
                ),
              Row(
                children: [
                  for (final entry in entries)
                    Expanded(
                      child: GestureDetector(
                        behavior: HitTestBehavior.opaque,
                        onTap: () => onChanged(entry.key),
                        child: Center(
                          child: Text(
                            entry.value,
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: entry.key == value
                                  ? FontWeight.w600
                                  : FontWeight.w500,
                              color: textColor,
                            ),
                          ),
                        ),
                      ),
                    ),
                ],
              ),
            ],
          ),
        );
      },
    );
  }
}
