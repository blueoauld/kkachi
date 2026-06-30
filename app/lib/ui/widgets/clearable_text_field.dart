import 'package:flutter/material.dart';

/// 입력값이 있을 때 우측에 클리어(X) 버튼이 표시되는 입력창.
///
/// 버튼을 누르면 [controller]의 텍스트를 비운다.
class ClearableTextField extends StatelessWidget {
  const ClearableTextField({
    super.key,
    required this.controller,
    this.hintText,
    this.keyboardType,
    this.textInputAction,
    this.obscureText = false,
    this.onSubmitted,
  });

  final TextEditingController controller;
  final String? hintText;
  final TextInputType? keyboardType;
  final TextInputAction? textInputAction;
  final bool obscureText;
  final ValueChanged<String>? onSubmitted;

  @override
  Widget build(BuildContext context) {
    // 텍스트 변화에 따라 클리어 버튼 표시 여부만 갱신한다.
    return ValueListenableBuilder<TextEditingValue>(
      valueListenable: controller,
      builder: (context, value, _) {
        return TextField(
          controller: controller,
          keyboardType: keyboardType,
          textInputAction: textInputAction,
          obscureText: obscureText,
          onSubmitted: onSubmitted,
          decoration: InputDecoration(
            hintText: hintText,
            suffixIcon: value.text.isEmpty
                ? null
                : IconButton(
                    icon: const Icon(Icons.cancel),
                    iconSize: 20,
                    color: Colors.grey,
                    onPressed: controller.clear,
                  ),
          ),
        );
      },
    );
  }
}
