import 'dart:async';

import 'package:flutter_riverpod/flutter_riverpod.dart';

/// 성별.
enum Gender { male, female }

/// 회원가입 화면의 상태.
class SignUpState {
  const SignUpState({
    this.gender,
    this.isSendingCode = false,
    this.isSubmitting = false,
  });

  final Gender? gender;
  final bool isSendingCode;
  final bool isSubmitting;

  SignUpState copyWith({
    Gender? gender,
    bool? isSendingCode,
    bool? isSubmitting,
  }) {
    return SignUpState(
      gender: gender ?? this.gender,
      isSendingCode: isSendingCode ?? this.isSendingCode,
      isSubmitting: isSubmitting ?? this.isSubmitting,
    );
  }
}

/// 회원가입 화면의 ViewModel.
///
/// 성별 선택 등 폼 상태를 보유하고, 인증번호 전송/회원가입 요청을 처리한다.
class SignUpViewModel extends Notifier<SignUpState> {
  /// 010으로 시작하는 11자리 숫자.
  static final _phoneRegExp = RegExp(r'^010\d{8}$');

  /// 6자리 숫자.
  static final _codeRegExp = RegExp(r'^\d{6}$');

  @override
  SignUpState build() => const SignUpState();

  void selectGender(Gender gender) {
    state = state.copyWith(gender: gender);
  }

  /// 휴대폰 번호로 인증번호를 전송한다.
  Future<void> sendVerificationCode({required String phone}) async {
    if (!_phoneRegExp.hasMatch(phone)) {
      throw const SignUpException('휴대폰 번호는 010으로 시작하는 11자리여야 합니다.');
    }
    state = state.copyWith(isSendingCode: true);
    try {
      // TODO: AuthRepository를 통해 인증번호 전송 요청을 연동한다.
      await Future<void>.delayed(const Duration(seconds: 1));
    } finally {
      state = state.copyWith(isSendingCode: false);
    }
  }

  /// 입력값을 검증하고 회원가입을 요청한다.
  Future<void> signUp({
    required String phone,
    required String code,
    required String password,
    required String passwordConfirm,
  }) async {
    if (phone.isEmpty ||
        code.isEmpty ||
        password.isEmpty ||
        passwordConfirm.isEmpty) {
      throw const SignUpException('모든 항목을 입력해주세요.');
    }
    if (!_phoneRegExp.hasMatch(phone)) {
      throw const SignUpException('휴대폰 번호는 010으로 시작하는 11자리여야 합니다.');
    }
    if (!_codeRegExp.hasMatch(code)) {
      throw const SignUpException('인증번호는 6자리 숫자여야 합니다.');
    }
    if (password != passwordConfirm) {
      throw const SignUpException('비밀번호가 일치하지 않습니다.');
    }
    if (state.gender == null) {
      throw const SignUpException('성별을 선택해주세요.');
    }

    state = state.copyWith(isSubmitting: true);
    try {
      // TODO: AuthRepository를 통해 회원가입 요청을 연동한다.
      throw const SignUpException('회원가입 기능은 아직 준비 중입니다.');
    } finally {
      state = state.copyWith(isSubmitting: false);
    }
  }
}

final signUpViewModelProvider = NotifierProvider<SignUpViewModel, SignUpState>(
  SignUpViewModel.new,
);

/// 회원가입 과정에서 발생하는 도메인 예외.
class SignUpException implements Exception {
  const SignUpException(this.message);

  final String message;

  @override
  String toString() => message;
}
