import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:go_router/go_router.dart';

import '../../router.dart';
import '../adaptive/adaptive_confirm_dialog.dart';
import '../widgets/app_primary_button.dart';

/// 성별 구분.
enum Gender { male, female }

/// 휴대폰 번호는 항상 010으로 시작하도록 강제하는 포맷터.
class _PhonePrefixFormatter extends TextInputFormatter {
  static const _prefix = '010';

  @override
  TextEditingValue formatEditUpdate(
    TextEditingValue oldValue,
    TextEditingValue newValue,
  ) {
    final text = newValue.text;
    // 입력 중인 길이만큼 010 접두사와 일치하는지 확인한다.
    for (var i = 0; i < text.length && i < _prefix.length; i++) {
      if (text[i] != _prefix[i]) return oldValue;
    }
    return newValue;
  }
}

/// 회원가입 화면. (현재는 UI 껍데기)
class SignUpView extends StatefulWidget {
  const SignUpView({super.key});

  @override
  State<SignUpView> createState() => _SignUpViewState();
}

class _SignUpViewState extends State<SignUpView> {
  final TextEditingController _phoneController = TextEditingController();
  final TextEditingController _codeController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _passwordConfirmController =
      TextEditingController();

  Gender? _gender;

  /// 휴대폰 번호가 010으로 시작하는 11자리인지 여부.
  bool get _isPhoneValid =>
      _phoneController.text.length == 11 &&
      _phoneController.text.startsWith('010');

  /// 모든 항목이 입력/선택됐는지 여부.
  bool get _canSubmit =>
      _isPhoneValid &&
      _codeController.text.length == 6 &&
      _passwordController.text.isNotEmpty &&
      _passwordConfirmController.text.isNotEmpty &&
      _passwordController.text == _passwordConfirmController.text &&
      _gender != null;

  @override
  void initState() {
    super.initState();
    _phoneController.addListener(_onInputChanged);
    _codeController.addListener(_onInputChanged);
    _passwordController.addListener(_onInputChanged);
    _passwordConfirmController.addListener(_onInputChanged);
    // 화면 진입 시 미성년자 이용 제한 안내를 띄운다.
    WidgetsBinding.instance.addPostFrameCallback((_) => _showNotice());
  }

  void _showNotice() {
    AdaptiveConfirmDialog.alert(
      context,
      title: '경고',
      message: '미성년자는 이용할 수 없습니다.\n적발 시 서비스 이용이 제한됩니다.',
      isTitleDestructive: true,
    );
  }

  @override
  void dispose() {
    _phoneController.dispose();
    _codeController.dispose();
    _passwordController.dispose();
    _passwordConfirmController.dispose();
    super.dispose();
  }

  void _onInputChanged() => setState(() {});

  void _onSendCode() {
    // TODO: 인증번호 전송 처리 연동
  }

  void _onSubmit() {
    // TODO: 회원가입 처리 연동. 다음 단계로 프로필 설정 화면으로 이동한다.
    context.push(AppRoutes.profileSetup);
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

  Widget _buildTextField(
    BuildContext context, {
    required TextEditingController controller,
    required String placeholder,
    TextInputType? keyboardType,
    bool obscureText = false,
    List<TextInputFormatter>? inputFormatters,
  }) {
    return _buildField(
      context,
      child: CupertinoTextField(
        controller: controller,
        keyboardType: keyboardType,
        obscureText: obscureText,
        placeholder: placeholder,
        padding: const EdgeInsets.all(14),
        decoration: const BoxDecoration(),
        clearButtonMode: OverlayVisibilityMode.editing,
        inputFormatters: inputFormatters,
      ),
    );
  }

  Widget _buildGenderSelector(BuildContext context) {
    return Row(
      children: [
        Expanded(child: _buildGenderOption(context, Gender.male, '남자')),
        const SizedBox(width: 12),
        Expanded(child: _buildGenderOption(context, Gender.female, '여자')),
      ],
    );
  }

  Widget _buildGenderOption(BuildContext context, Gender gender, String label) {
    final selected = _gender == gender;
    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: () => setState(() => _gender = gender),
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 14),
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: selected
              ? CupertinoColors.activeBlue
              : CupertinoColors.systemGrey6.resolveFrom(context),
          borderRadius: BorderRadius.circular(12),
        ),
        child: Text(
          label,
          style: TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.w600,
            color: selected
                ? CupertinoColors.white
                : CupertinoColors.label.resolveFrom(context),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        border: null,
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('회원가입'),
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
                      IntrinsicHeight(
                        child: Row(
                          crossAxisAlignment: CrossAxisAlignment.stretch,
                          children: [
                            Expanded(
                              child: _buildTextField(
                                context,
                                controller: _phoneController,
                                placeholder: '휴대폰',
                                keyboardType: TextInputType.phone,
                                inputFormatters: [
                                  FilteringTextInputFormatter.digitsOnly,
                                  LengthLimitingTextInputFormatter(11),
                                  _PhonePrefixFormatter(),
                                ],
                              ),
                            ),
                            const SizedBox(width: 12),
                            CupertinoButton(
                              color: CupertinoColors.activeBlue,
                              padding: const EdgeInsets.symmetric(
                                horizontal: 16,
                              ),
                              minimumSize: Size.zero,
                              onPressed: _isPhoneValid ? _onSendCode : null,
                              child: const Text(
                                '전송',
                                style: TextStyle(
                                  color: CupertinoColors.white,
                                  fontWeight: FontWeight.w600,
                                  fontSize: 16,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 12),
                      _buildTextField(
                        context,
                        controller: _codeController,
                        placeholder: '인증번호',
                        keyboardType: TextInputType.number,
                        inputFormatters: [
                          FilteringTextInputFormatter.digitsOnly,
                          LengthLimitingTextInputFormatter(6),
                        ],
                      ),
                      const SizedBox(height: 12),
                      _buildTextField(
                        context,
                        controller: _passwordController,
                        placeholder: '비밀번호',
                        obscureText: true,
                      ),
                      const SizedBox(height: 12),
                      _buildTextField(
                        context,
                        controller: _passwordConfirmController,
                        placeholder: '비밀번호 확인',
                        obscureText: true,
                      ),
                      const SizedBox(height: 12),
                      _buildGenderSelector(context),
                    ],
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(16),
                child: AppPrimaryButton(
                  label: '회원가입',
                  onPressed: _canSubmit ? _onSubmit : null,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
