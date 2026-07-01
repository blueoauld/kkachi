import { Box } from "@/components/ui/box";
import { Button, ButtonText } from "@/components/ui/button";
import { HStack } from "@/components/ui/hstack";
import { Input, InputField } from "@/components/ui/input";
import { VStack } from "@/components/ui/vstack";
import { useHeaderHeight } from "expo-router/react-navigation";
import {
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  TouchableWithoutFeedback,
} from "react-native";

export default function SignupScreen() {
  const headerHeight = useHeaderHeight();

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      keyboardVerticalOffset={headerHeight}
    >
      <TouchableWithoutFeedback onPress={Keyboard.dismiss} accessible={false}>
        <Box className="flex-1 p-6">
          <VStack space="lg">
            <VStack space="sm">
              <HStack space="sm">
                <Input className="flex-1">
                  <InputField
                    type="text"
                    keyboardType="phone-pad"
                    inputMode="tel"
                    placeholder="휴대폰 번호"
                    className="text-lg"
                    maxLength={11}
                  />
                </Input>
                <Button variant="default" size="lg">
                  <ButtonText className="text-lg">전송</ButtonText>
                </Button>
              </HStack>
              <Input>
                <InputField
                  type="text"
                  keyboardType="numeric"
                  inputMode="numeric"
                  placeholder="인증번호"
                  className="text-lg"
                  maxLength={6}
                />
              </Input>
            </VStack>
            <VStack space="sm">
              <Input>
                <InputField
                  type="password"
                  placeholder="비밀번호"
                  className="text-lg"
                />
              </Input>
              <Input>
                <InputField
                  type="password"
                  placeholder="비밀번호 확인"
                  className="text-lg"
                />
              </Input>
            </VStack>
            <HStack space="sm">
              <Button variant="secondary" size="lg" className="flex-1 py-2">
                <ButtonText className="text-lg">남자</ButtonText>
              </Button>
              <Button variant="default" size="lg" className="flex-1 py-2">
                <ButtonText className="text-lg">여자</ButtonText>
              </Button>
            </HStack>
          </VStack>

          <Box className="mt-auto">
            <Button variant="default" size="lg" className="w-full py-4">
              <ButtonText className="text-lg font-semibold">회원가입</ButtonText>
            </Button>
          </Box>
        </Box>
      </TouchableWithoutFeedback>
    </KeyboardAvoidingView>
  );
}
