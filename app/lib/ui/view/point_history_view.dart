import 'package:app/model/point_transaction.dart';
import 'package:flutter/cupertino.dart';

/// 포인트 내역 화면. (현재는 mock 데이터)
class PointHistoryView extends StatelessWidget {
  const PointHistoryView({super.key});

  /// 보유 포인트. (TODO: API 연동)
  static const int _points = 100;

  /// mock 포인트 적립/사용 내역. (TODO: API 연동)
  static const List<PointTransaction> _transactions = [
    PointTransaction(title: '출석 체크', dateLabel: '2026.06.28', amount: 10),
    PointTransaction(title: '광고 보상', dateLabel: '2026.06.27', amount: 20),
    PointTransaction(title: '비밀 사진 열람', dateLabel: '2026.06.27', amount: -30),
    PointTransaction(title: '메시지 전송', dateLabel: '2026.06.26', amount: -10),
    PointTransaction(title: '출석 체크', dateLabel: '2026.06.26', amount: 10),
    PointTransaction(title: '광고 보상', dateLabel: '2026.06.25', amount: 20),
    PointTransaction(title: '회원가입 보너스', dateLabel: '2026.06.25', amount: 80),
  ];

  @override
  Widget build(BuildContext context) {
    final backgroundColor = CupertinoColors.systemGroupedBackground.resolveFrom(
      context,
    );
    final contentColor = CupertinoColors.secondarySystemGroupedBackground
        .resolveFrom(context);
    final secondaryColor = CupertinoColors.secondaryLabel.resolveFrom(context);

    return CupertinoPageScaffold(
      backgroundColor: backgroundColor,
      navigationBar: CupertinoNavigationBar(
        border: null,
        backgroundColor: backgroundColor,
        middle: const Text('포인트 내역'),
      ),
      child: SafeArea(
        bottom: false,
        child: CustomScrollView(
          slivers: [
            // 보유 포인트 헤더.
            SliverToBoxAdapter(
              child: Container(
                margin: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                padding: const EdgeInsets.symmetric(vertical: 28),
                decoration: BoxDecoration(
                  color: contentColor,
                  borderRadius: const BorderRadius.all(Radius.circular(12)),
                ),
                alignment: Alignment.center,
                child: Column(
                  children: [
                    Text(
                      '보유 포인트',
                      style: TextStyle(fontSize: 14, color: secondaryColor),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      '${_points}P',
                      style: const TextStyle(
                        fontSize: 36,
                        fontWeight: FontWeight.w700,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                child: Text(
                  '내역',
                  style: TextStyle(
                    fontSize: 13,
                    fontWeight: FontWeight.w600,
                    color: secondaryColor,
                  ),
                ),
              ),
            ),
            SliverPadding(
              padding: EdgeInsets.only(
                bottom: 16 + MediaQuery.of(context).padding.bottom,
              ),
              sliver: SliverList.separated(
                itemCount: _transactions.length,
                itemBuilder: (context, index) =>
                    _TransactionTile(transaction: _transactions[index]),
                separatorBuilder: (context, index) => Container(
                  height: 0.5,
                  margin: const EdgeInsets.only(left: 16),
                  color: CupertinoColors.separator.resolveFrom(context),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _TransactionTile extends StatelessWidget {
  const _TransactionTile({required this.transaction});

  final PointTransaction transaction;

  @override
  Widget build(BuildContext context) {
    final secondaryColor = CupertinoColors.secondaryLabel.resolveFrom(context);
    final amountColor = transaction.isEarned
        ? CupertinoColors.systemRed.resolveFrom(context)
        : CupertinoColors.systemBlue.resolveFrom(context);
    final sign = transaction.isEarned ? '+' : '-';

    return Container(
      color: CupertinoColors.secondarySystemGroupedBackground.resolveFrom(
        context,
      ),
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 14),
      child: Row(
        children: [
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  transaction.title,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  transaction.dateLabel,
                  style: TextStyle(fontSize: 13, color: secondaryColor),
                ),
              ],
            ),
          ),
          Text(
            '$sign${transaction.amount.abs()}P',
            style: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.w600,
              color: amountColor,
            ),
          ),
        ],
      ),
    );
  }
}
