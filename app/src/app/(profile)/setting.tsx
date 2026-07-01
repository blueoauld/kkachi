import { Box } from "@/components/ui/box";
import { Button, ButtonText } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { HStack } from "@/components/ui/hstack";
import { Input, InputField } from "@/components/ui/input";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";
import { useHeaderHeight } from "expo-router/react-navigation";
import {
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  TouchableWithoutFeedback,
} from "react-native";

export default function ProfileSettingScreen() {
  const headerHeight = useHeaderHeight();

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === "ios" ? "padding" : "height"}
      keyboardVerticalOffset={headerHeight}
    >
      <TouchableWithoutFeedback onPress={Keyboard.dismiss} accessible={false}>
        <Box className="flex-1">
          <ScrollView
            contentContainerStyle={{ flexGrow: 1, padding: 24 }}
            keyboardShouldPersistTaps="handled"
            className="flex-1"
          >
            <VStack space="lg">
              <VStack space="sm">
                <Heading size="sm">공개 사진</Heading>
                <HStack space="sm" className="w-full">
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                </HStack>
              </VStack>
              <VStack space="sm">
                <Heading size="sm">비밀 사진</Heading>
                <HStack space="sm" className="w-full">
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                  <Pressable
                    onPress={() => console.log("사진 업로드")}
                    className="flex-1 aspect-square rounded-md border border-border bg-secondary justify-center items-center"
                  >
                    <Text className="text-2xl">+</Text>
                  </Pressable>
                </HStack>
              </VStack>
              <VStack space="sm">
                <Heading size="sm">닉네임</Heading>
                <Input>
                  <InputField
                    type="text"
                    keyboardType="default"
                    inputMode="text"
                    placeholder="홍길동"
                    className="text-lg"
                    maxLength={10}
                  />
                </Input>
              </VStack>
              <VStack space="sm">
                <Heading size="sm">출생연도</Heading>
                <Input>
                  <InputField
                    type="text"
                    keyboardType="numeric"
                    inputMode="numeric"
                    placeholder="1995"
                    className="text-lg"
                    maxLength={4}
                  />
                </Input>
              </VStack>
              <VStack space="sm">
                <Heading size="sm">자기소개</Heading>
                <Input className="h-32 py-2">
                  <InputField
                    type="text"
                    keyboardType="default"
                    inputMode="text"
                    placeholder="내용 입력"
                    className="text-lg"
                    maxLength={1000}
                    multiline
                    textAlignVertical="top"
                  />
                </Input>
              </VStack>
            </VStack>
          </ScrollView>

          <Box className="border-t border-border bg-background p-6">
            <Button variant="default" size="lg" className="w-full py-4">
              <ButtonText className="text-lg font-semibold">확인</ButtonText>
            </Button>
          </Box>
        </Box>
      </TouchableWithoutFeedback>
    </KeyboardAvoidingView>
  );
}
