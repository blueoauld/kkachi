import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

/// 로그인 화면의 ViewModel.
///
/// 입력 검증과 로그인 요청을 담당하며, 진행 상태(로딩/에러)는 [AsyncValue]로
/// 표현한다. View는 [loginViewModelProvider]를 구독하여 상태를 반영한다.
class LoginViewModel extends AsyncNotifier<void> {
  @override
  FutureOr<void> build() {}

  /// 휴대폰 번호와 비밀번호로 로그인을 시도한다.
  Future<void> login({required String phone, required String password}) async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(() async {
      if (phone.trim().isEmpty || password.isEmpty) {
        throw const LoginException('휴대폰 번호와 비밀번호를 입력해주세요.');
      }

      // TODO: AuthRepository를 통해 실제 로그인 요청을 연동한다.
      throw const LoginException('로그인 기능은 아직 준비 중입니다.');
    });
  }
}

final loginViewModelProvider = AsyncNotifierProvider<LoginViewModel, void>(
  LoginViewModel.new,
);

/// 로그인 과정에서 발생하는 도메인 예외.
class LoginException implements Exception {
  const LoginException(this.message);

  final String message;

  @override
  String toString() => message;
}
