import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../widgets/clearable_text_field.dart';
import '../widgets/primary_button.dart';
import 'login_viewmodel.dart';

/// 로그인 화면.
///
/// 휴대폰 번호/비밀번호 입력, 회원가입 화면 이동 링크, 로그인 버튼으로 구성된다.
class LoginView extends ConsumerStatefulWidget {
  const LoginView({super.key});

  @override
  ConsumerState<LoginView> createState() => _LoginViewState();
}

class _LoginViewState extends ConsumerState<LoginView> {
  final _phoneController = TextEditingController();
  final _passwordController = TextEditingController();

  @override
  void dispose() {
    _phoneController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _onLoginPressed() {
    ref
        .read(loginViewModelProvider.notifier)
        .login(
          phone: _phoneController.text,
          password: _passwordController.text,
        );
  }

  void _onSignUpPressed() {
    // TODO: 회원가입 화면으로 이동 (go_router 라우트 연결 예정)
  }

  @override
  Widget build(BuildContext context) {
    final loginState = ref.watch(loginViewModelProvider);
    final isLoading = loginState.isLoading;

    // 로그인 실패 시 에러 메시지를 노출한다.
    ref.listen(loginViewModelProvider, (previous, next) {
      if (next.hasError && !next.isLoading) {
        ScaffoldMessenger.of(context)
          ..hideCurrentSnackBar()
          ..showSnackBar(SnackBar(content: Text('${next.error}')));
      }
    });

    return Scaffold(
      appBar: AppBar(title: const Text('로그인'), centerTitle: true),
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            return SingleChildScrollView(
              child: ConstrainedBox(
                constraints: BoxConstraints(minHeight: constraints.maxHeight),
                child: IntrinsicHeight(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        const SizedBox(height: 24),
                        ClearableTextField(
                          controller: _phoneController,
                          hintText: '휴대폰 번호',
                          keyboardType: TextInputType.phone,
                          textInputAction: TextInputAction.next,
                        ),
                        const SizedBox(height: 8),
                        ClearableTextField(
                          controller: _passwordController,
                          hintText: '비밀번호',
                          obscureText: true,
                          textInputAction: TextInputAction.done,
                          onSubmitted: (_) => _onLoginPressed(),
                        ),
                        const SizedBox(height: 8),
                        Center(
                          child: TextButton(
                            onPressed: _onSignUpPressed,
                            style: TextButton.styleFrom(
                              splashFactory: NoSplash.splashFactory,
                            ),
                            child: const Text('회원가입'),
                          ),
                        ),
                        const Spacer(),
                        PrimaryButton(
                          label: '로그인',
                          isLoading: isLoading,
                          onPressed: _onLoginPressed,
                        ),
                        const SizedBox(height: 16),
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
