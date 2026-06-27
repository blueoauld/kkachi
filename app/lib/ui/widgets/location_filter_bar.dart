import 'package:flutter/cupertino.dart';

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
              child: Text('위치'),
            ),
          },
        ),
      ),
    );
  }
}
