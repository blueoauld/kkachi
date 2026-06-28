import 'package:app/ui/adaptive/adaptive_segmented_control.dart';
import 'package:flutter/cupertino.dart';

/// 전체 / 위치 필터 세그먼트 컨트롤.
///
/// value — 0: 전체, 1: 위치
class LocationFilterBar extends StatelessWidget {
  const LocationFilterBar({
    super.key,
    required this.value,
    required this.onChanged,
  });

  final int value;
  final ValueChanged<int> onChanged;

  @override
  Widget build(BuildContext context) {
    return AdaptiveSegmentedControl(
      value: value,
      onChanged: onChanged,
      labels: const {0: '전체', 1: '위치'},
    );
  }
}
