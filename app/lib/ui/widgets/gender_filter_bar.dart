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
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
      child: SizedBox(
        width: double.infinity,
        child: CupertinoSlidingSegmentedControl<int>(
          groupValue: value,
          onValueChanged: (v) {
            if (v != null) onChanged(v);
          },
          children: const {
            0: Padding(
              padding: EdgeInsets.symmetric(vertical: 6),
              child: Text('전체'),
            ),
            1: Padding(
              padding: EdgeInsets.symmetric(vertical: 6),
              child: Text('남자'),
            ),
            2: Padding(
              padding: EdgeInsets.symmetric(vertical: 6),
              child: Text('여자'),
            ),
          },
        ),
      ),
    );
  }
}
