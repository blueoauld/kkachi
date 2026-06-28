/// 포인트 적립/사용 내역 한 건.
class PointTransaction {
  const PointTransaction({
    required this.title,
    required this.dateLabel,
    required this.amount,
  });

  final String title;
  final String dateLabel;

  /// 변동 포인트. 양수는 적립, 음수는 사용.
  final int amount;

  bool get isEarned => amount >= 0;
}
