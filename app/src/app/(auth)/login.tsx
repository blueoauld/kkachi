import { Box } from "@/components/ui/box";
import { Button, ButtonText } from "@/components/ui/button";
import { Input, InputField } from "@/components/ui/input";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";
import { router } from "expo-router";
import { useHeaderHeight } from "expo-router/react-navigation";
import {
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  TouchableWithoutFeedback,
} from "react-native";

export default function LoginScreen() {
  const headerHeight = useHeaderHeight();

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      keyboardVerticalOffset={headerHeight}
    >
      <TouchableWithoutFeedback
        onPress={Keyboard.dismiss}
        accessible={false}
      >
        <Box className="flex-1 p-6">
          <VStack space="lg">
            <Input>
              <InputField
                type="text"
                keyboardType="phone-pad"
                inputMode="numeric"
                placeholder="휴대폰 번호"
                className="text-lg"
                maxLength={11}
              />
            </Input>
            <Input>
              <InputField
                type="password"
                placeholder="비밀번호"
                className="text-lg"
              />
            </Input>
            <Pressable
              onPress={() => router.push("/signup")}
              className="items-center"
            >
              <Text>회원가입</Text>
            </Pressable>
          </VStack>

          <Box className="mt-auto">
            <Button variant="default" size="lg" className="w-full py-4">
              <ButtonText className="text-lg font-semibold">로그인</ButtonText>
            </Button>
          </Box>
        </Box>
      </TouchableWithoutFeedback>
    </KeyboardAvoidingView>
  );
}
