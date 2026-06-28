import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';

import '../../router.dart';
import '../widgets/app_primary_button.dart';

/// 로그인 화면. (현재는 UI 껍데기)
class LoginView extends StatefulWidget {
  const LoginView({super.key});

  @override
  State<LoginView> createState() => _LoginViewState();
}

class _LoginViewState extends State<LoginView> {
  final TextEditingController _phoneController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  /// 휴대폰 번호와 비밀번호가 모두 입력됐는지 여부.
  bool get _canLogin =>
      _phoneController.text.trim().isNotEmpty &&
      _passwordController.text.isNotEmpty;

  @override
  void initState() {
    super.initState();
    _phoneController.addListener(_onInputChanged);
    _passwordController.addListener(_onInputChanged);
  }

  @override
  void dispose() {
    _phoneController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _onInputChanged() => setState(() {});

  void _onLogin() {
    // TODO: 로그인 처리 연동. 지금은 바로 메인으로 이동한다.
    context.go(AppRoutes.main);
  }

  void _onSignUp() {
    context.push(AppRoutes.signup);
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
    return CupertinoPageScaffold(
      navigationBar: const CupertinoNavigationBar(
        backgroundColor: CupertinoColors.systemBackground,
        middle: Text('로그인'),
      ),
      child: GestureDetector(
        behavior: HitTestBehavior.opaque,
        onTap: () => FocusScope.of(context).unfocus(),
        child: SafeArea(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                const SizedBox(height: 24),
                _buildField(
                  context,
                  child: CupertinoTextField(
                    controller: _phoneController,
                    keyboardType: TextInputType.phone,
                    placeholder: '휴대폰',
                    padding: const EdgeInsets.all(14),
                    decoration: const BoxDecoration(),
                    clearButtonMode: OverlayVisibilityMode.editing,
                  ),
                ),
                const SizedBox(height: 12),
                _buildField(
                  context,
                  child: CupertinoTextField(
                    controller: _passwordController,
                    obscureText: true,
                    placeholder: '비밀번호',
                    padding: const EdgeInsets.all(14),
                    decoration: const BoxDecoration(),
                    clearButtonMode: OverlayVisibilityMode.editing,
                  ),
                ),
                const SizedBox(height: 16),
                Center(
                  child: CupertinoButton(
                    padding: EdgeInsets.zero,
                    minimumSize: Size.zero,
                    onPressed: _onSignUp,
                    child: const Text(
                      '회원가입',
                      style: TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w600,
                        color: CupertinoColors.activeBlue,
                      ),
                    ),
                  ),
                ),
                const Spacer(),
                AppPrimaryButton(
                  label: '로그인',
                  onPressed: _canLogin ? _onLogin : null,
                ),
                const SizedBox(height: 16),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
