import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../core/app_theme.dart';
import '../widgets/clearable_text_field.dart';
import '../widgets/primary_button.dart';
import 'signup_viewmodel.dart';

/// 회원가입 화면.
///
/// 휴대폰 번호 + 인증번호 전송, 인증번호 입력, 비밀번호/비밀번호 확인,
/// 성별 선택으로 구성된다.
class SignUpView extends ConsumerStatefulWidget {
  const SignUpView({super.key});

  @override
  ConsumerState<SignUpView> createState() => _SignUpViewState();
}

class _SignUpViewState extends ConsumerState<SignUpView> {
  final _phoneController = TextEditingController();
  final _codeController = TextEditingController();
  final _passwordController = TextEditingController();
  final _passwordConfirmController = TextEditingController();

  @override
  void dispose() {
    _phoneController.dispose();
    _codeController.dispose();
    _passwordController.dispose();
    _passwordConfirmController.dispose();
    super.dispose();
  }

  SignUpViewModel get _viewModel => ref.read(signUpViewModelProvider.notifier);

  void _showMessage(String message) {
    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text(message)));
  }

  Future<void> _onSendCode() async {
    try {
      await _viewModel.sendVerificationCode(phone: _phoneController.text);
      if (mounted) _showMessage('인증번호를 전송했습니다.');
    } catch (e) {
      if (mounted) _showMessage('$e');
    }
  }

  Future<void> _onSubmit() async {
    try {
      await _viewModel.signUp(
        phone: _phoneController.text,
        code: _codeController.text,
        password: _passwordController.text,
        passwordConfirm: _passwordConfirmController.text,
      );
    } catch (e) {
      if (mounted) _showMessage('$e');
    }
  }

  Widget _genderButton(Gender gender, String label, SignUpState state) {
    final isSelected = state.gender == gender;
    final colorScheme = Theme.of(context).colorScheme;
    final fieldColor = Theme.of(context).inputDecorationTheme.fillColor;
    return InkWell(
      borderRadius: AppRadius.fieldBorderRadius,
      onTap: () => _viewModel.selectGender(gender),
      child: Container(
        height: 48,
        alignment: Alignment.center,
        decoration: BoxDecoration(
          color: isSelected ? colorScheme.primary : fieldColor,
          borderRadius: AppRadius.fieldBorderRadius,
        ),
        child: Text(
          label,
          style: TextStyle(
            color: isSelected ? colorScheme.onPrimary : Colors.grey,
            fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final state = ref.watch(signUpViewModelProvider);

    return Scaffold(
      appBar: AppBar(title: const Text('회원가입'), centerTitle: true),
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            return SingleChildScrollView(
              child: ConstrainedBox(
                constraints: BoxConstraints(minHeight: constraints.maxHeight),
                child: IntrinsicHeight(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16,
                      vertical: 24,
                    ),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        // 휴대폰 번호 + 인증번호 전송
                        Row(
                          children: [
                            Expanded(
                              child: ClearableTextField(
                                controller: _phoneController,
                                hintText: '휴대폰 번호',
                                keyboardType: TextInputType.phone,
                                textInputAction: TextInputAction.next,
                                inputFormatters: [
                                  FilteringTextInputFormatter.digitsOnly,
                                  LengthLimitingTextInputFormatter(11),
                                ],
                              ),
                            ),
                            const SizedBox(width: 8),
                            SizedBox(
                              width: 80,
                              height: 52,
                              child: FilledButton(
                                style: FilledButton.styleFrom(
                                  elevation: 0,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: AppRadius.fieldBorderRadius,
                                  ),
                                ),
                                onPressed: state.isSendingCode
                                    ? null
                                    : _onSendCode,
                                child: state.isSendingCode
                                    ? const SizedBox(
                                        height: 20,
                                        width: 20,
                                        child:
                                            CircularProgressIndicator.adaptive(
                                              strokeWidth: 2,
                                            ),
                                      )
                                    : const Text('전송'),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        ClearableTextField(
                          controller: _codeController,
                          hintText: '인증번호',
                          keyboardType: TextInputType.number,
                          textInputAction: TextInputAction.next,
                          inputFormatters: [
                            FilteringTextInputFormatter.digitsOnly,
                            LengthLimitingTextInputFormatter(6),
                          ],
                        ),
                        const SizedBox(height: 16),
                        ClearableTextField(
                          controller: _passwordController,
                          hintText: '비밀번호',
                          obscureText: true,
                          textInputAction: TextInputAction.next,
                        ),
                        const SizedBox(height: 8),
                        ClearableTextField(
                          controller: _passwordConfirmController,
                          hintText: '비밀번호 확인',
                          obscureText: true,
                          textInputAction: TextInputAction.done,
                        ),
                        const SizedBox(height: 16),
                        // 성별 선택
                        Row(
                          children: [
                            Expanded(
                              child: _genderButton(Gender.male, '남자', state),
                            ),
                            const SizedBox(width: 8),
                            Expanded(
                              child: _genderButton(Gender.female, '여자', state),
                            ),
                          ],
                        ),
                        const Spacer(),
                        const SizedBox(height: 24),
                        PrimaryButton(
                          label: '회원가입',
                          isLoading: state.isSubmitting,
                          onPressed: _onSubmit,
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
