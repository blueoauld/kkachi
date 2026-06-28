import 'package:flutter/cupertino.dart';

import '../widgets/app_primary_button.dart';

/// 신고 카테고리 목록.
const _reportCategories = <String>[
  '욕설 / 비방',
  '스팸 / 광고',
  '미성년자',
  '음란물',
  '도용',
  '기타',
];

/// 회원 신고하기 화면. (현재는 UI 껍데기)
class ReportView extends StatefulWidget {
  const ReportView({super.key, required this.nickname});

  final String nickname;

  @override
  State<ReportView> createState() => _ReportViewState();
}

class _ReportViewState extends State<ReportView> {
  String? _selectedCategory;
  final TextEditingController _reasonController = TextEditingController();

  // TODO: 증거 이미지 첨부 기능 연동
  final List<Object> _evidenceImages = [];

  @override
  void dispose() {
    _reasonController.dispose();
    super.dispose();
  }

  void _onAttachImage() {
    // TODO: 이미지 피커 연동
  }

  void _onSubmit() {
    // TODO: 신고 제출 처리
  }

  Widget _buildSectionLabel(BuildContext context, String text) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Text(
        text,
        style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w600),
      ),
    );
  }

  Widget _buildCategoryList(BuildContext context) {
    return Column(
      children: [
        for (final category in _reportCategories) ...[
          GestureDetector(
            behavior: HitTestBehavior.opaque,
            onTap: () => setState(() => _selectedCategory = category),
            child: Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: CupertinoColors.systemGrey6.resolveFrom(context),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(category, style: const TextStyle(fontSize: 16)),
                  ),
                  // 선택 여부와 무관하게 아이콘 자리를 고정해 행 높이를 유지한다.
                  SizedBox(
                    width: 20,
                    height: 20,
                    child: category == _selectedCategory
                        ? Icon(
                            CupertinoIcons.checkmark_alt,
                            size: 20,
                            color: CupertinoColors.activeBlue.resolveFrom(
                              context,
                            ),
                          )
                        : null,
                  ),
                ],
              ),
            ),
          ),
          if (category != _reportCategories.last) const SizedBox(height: 8),
        ],
      ],
    );
  }

  Widget _buildImageAttach(BuildContext context) {
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: _onAttachImage,
      child: Container(
        height: 96,
        width: 96,
        decoration: BoxDecoration(
          color: CupertinoColors.systemGrey6.resolveFrom(context),
          borderRadius: BorderRadius.circular(12),
        ),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              CupertinoIcons.camera,
              size: 28,
              color: CupertinoColors.systemGrey.resolveFrom(context),
            ),
            const SizedBox(height: 6),
            Text(
              '${_evidenceImages.length} / 5',
              style: TextStyle(
                fontSize: 13,
                color: CupertinoColors.systemGrey.resolveFrom(context),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildReasonField(BuildContext context) {
    return CupertinoTextField(
      controller: _reasonController,
      maxLines: 6,
      minLines: 6,
      placeholder: '내용 입력',
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: CupertinoColors.systemGrey6.resolveFrom(context),
        borderRadius: BorderRadius.circular(12),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: CupertinoNavigationBar(
        border: null,
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('신고하기 (${widget.nickname})'),
      ),
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: () => FocusScope.of(context).unfocus(),
        child: SafeArea(
          child: Column(
            children: [
              Expanded(
                child: SingleChildScrollView(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      _buildSectionLabel(context, '카테고리'),
                      _buildCategoryList(context),
                      const SizedBox(height: 24),
                      _buildSectionLabel(context, '증거 사진'),
                      _buildImageAttach(context),
                      const SizedBox(height: 24),
                      _buildSectionLabel(context, '상세 내용'),
                      _buildReasonField(context),
                    ],
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(16),
                child: AppPrimaryButton(
                  label: '신고하기',
                  color: CupertinoColors.systemRed,
                  onPressed: _selectedCategory == null ? null : _onSubmit,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
