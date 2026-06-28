import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:go_router/go_router.dart';

import '../../router.dart';
import '../widgets/app_primary_button.dart';

/// 프로필 설정 화면. (현재는 UI 껍데기)
class ProfileSetupView extends StatefulWidget {
  const ProfileSetupView({super.key});

  @override
  State<ProfileSetupView> createState() => _ProfileSetupViewState();
}

class _ProfileSetupViewState extends State<ProfileSetupView> {
  /// 가입 가능한 최소 나이. (미성년자 제외)
  static const _minAge = 19;

  /// 가입 가능한 최대 나이.
  static const _maxAge = 50;

  /// 프로필 / 비밀 사진 슬롯. (TODO: 이미지 피커 연동)
  final List<Object?> _profileImages = [];
  final List<Object?> _secretImages = [];

  final TextEditingController _nicknameController = TextEditingController();
  final TextEditingController _birthYearController = TextEditingController();
  final TextEditingController _bioController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _nicknameController.addListener(_onInputChanged);
    _birthYearController.addListener(_onInputChanged);
  }

  @override
  void dispose() {
    _nicknameController.dispose();
    _birthYearController.dispose();
    _bioController.dispose();
    super.dispose();
  }

  void _onInputChanged() => setState(() {});

  /// 닉네임이 2~10자인지 여부.
  bool get _isNicknameValid {
    final length = _nicknameController.text.trim().length;
    return length >= 2 && length <= 10;
  }

  /// 출생연도가 4자리이고 나이가 19~50세 범위인지 여부. (현재 연도 기준)
  bool get _isBirthYearValid {
    if (_birthYearController.text.length != 4) return false;
    final year = int.tryParse(_birthYearController.text);
    if (year == null) return false;
    final age = DateTime.now().year - year;
    return age >= _minAge && age <= _maxAge;
  }

  /// 필수 항목이 채워졌는지 여부.
  bool get _canSubmit => _isNicknameValid && _isBirthYearValid;

  void _onTapPhoto(List<Object?> images) {
    // TODO: 이미지 피커 연동
  }

  void _onSubmit() {
    // TODO: 프로필 저장 처리 연동. 저장 후 메인 화면으로 이동한다.
    context.go(AppRoutes.main);
  }

  Widget _buildSectionLabel(String text) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Text(
        text,
        style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w600),
      ),
    );
  }

  // TODO: 사진 여러 장 추가 지원. 현재는 추가 버튼 한 개만 노출한다.
  Widget _buildPhotoAddSlot(BuildContext context, List<Object?> images) {
    return Align(
      alignment: Alignment.centerLeft,
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: () => _onTapPhoto(images),
        child: Container(
          width: 96,
          height: 96,
          decoration: BoxDecoration(
            color: CupertinoColors.systemGrey6.resolveFrom(context),
            borderRadius: BorderRadius.circular(12),
          ),
          child: Icon(
            CupertinoIcons.add,
            size: 28,
            color: CupertinoColors.systemGrey.resolveFrom(context),
          ),
        ),
      ),
    );
  }

  Widget _buildField(BuildContext context, {required Widget child}) {
    return Container(
      decoration: BoxDecoration(
        color: CupertinoColors.systemGrey6.resolveFrom(context),
        borderRadius: BorderRadius.circular(12),
      ),
      child: child,
    );
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      // 프로필 설정은 가입 필수 단계라 뒤로가기를 막는다.
      canPop: false,
      child: CupertinoPageScaffold(
        navigationBar: const CupertinoNavigationBar(
          border: null,
          backgroundColor: CupertinoColors.systemBackground,
          automaticallyImplyLeading: false,
          middle: Text('프로필 설정'),
        ),
        child: GestureDetector(
          behavior: HitTestBehavior.opaque,
          onTap: () => FocusScope.of(context).unfocus(),
          child: SafeArea(
            child: Column(
              children: [
                Expanded(
                  child: SingleChildScrollView(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16,
                      vertical: 24,
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        _buildSectionLabel('프로필 사진'),
                        _buildPhotoAddSlot(context, _profileImages),
                        const SizedBox(height: 24),
                        _buildSectionLabel('비밀 사진'),
                        _buildPhotoAddSlot(context, _secretImages),
                        const SizedBox(height: 24),
                        _buildSectionLabel('닉네임'),
                        _buildField(
                          context,
                          child: CupertinoTextField(
                            controller: _nicknameController,
                            placeholder: '홍길동',
                            padding: const EdgeInsets.all(14),
                            decoration: const BoxDecoration(),
                            clearButtonMode: OverlayVisibilityMode.editing,
                            maxLength: 10,
                          ),
                        ),
                        const SizedBox(height: 24),
                        _buildSectionLabel('출생연도'),
                        _buildField(
                          context,
                          child: CupertinoTextField(
                            controller: _birthYearController,
                            keyboardType: TextInputType.number,
                            placeholder: '1995',
                            padding: const EdgeInsets.all(14),
                            decoration: const BoxDecoration(),
                            inputFormatters: [
                              FilteringTextInputFormatter.digitsOnly,
                              LengthLimitingTextInputFormatter(4),
                            ],
                          ),
                        ),
                        const SizedBox(height: 24),
                        _buildSectionLabel('자기소개'),
                        _buildField(
                          context,
                          child: CupertinoTextField(
                            controller: _bioController,
                            minLines: 5,
                            maxLines: 10,
                            placeholder: '내용 입력',
                            padding: const EdgeInsets.all(14),
                            decoration: const BoxDecoration(),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(16),
                  child: AppPrimaryButton(
                    label: '완료',
                    onPressed: _canSubmit ? _onSubmit : null,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
