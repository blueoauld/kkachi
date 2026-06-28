import 'package:app/ui/adaptive/adaptive_segmented_control.dart';
import 'package:flutter/cupertino.dart';

/// 전체 / 남자 / 여자 성별 필터 세그먼트 컨트롤.
///
/// value — 0: 전체, 1: 남자, 2: 여자
class GenderFilterBar extends StatelessWidget {
  const GenderFilterBar({
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
      labels: const {0: '전체', 1: '남자', 2: '여자'},
    );
  }
}
