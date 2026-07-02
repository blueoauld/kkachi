import { Stack, useLocalSearchParams } from "expo-router";
import { useHeaderHeight } from "expo-router/react-navigation";
import { useState } from "react";
import {
  Keyboard,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  TouchableWithoutFeedback,
} from "react-native";

import { Box } from "@/components/ui/box";
import { Button, ButtonText } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { HStack } from "@/components/ui/hstack";
import { Input, InputField } from "@/components/ui/input";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

const REPORT_TYPES = [
  "욕설 / 비방",
  "스팸 / 광고",
  "미성년자",
  "음란물",
  "도용",
  "기타",
] as const;

const EVIDENCE_PHOTO_SLOT_COUNT = 5;

function ReportTypeRow({
  label,
  selected,
  onPress,
}: {
  label: string;
  selected: boolean;
  onPress: () => void;
}) {
  return (
    <Pressable
      onPress={onPress}
      className={`flex-row items-center justify-between p-4`}
    >
      <Text className="text-base text-foreground">{label}</Text>
      <Box
        className={`h-5 w-5 items-center justify-center rounded-full border-2 ${
          selected ? "border-primary bg-primary" : "border-border"
        }`}
      >
        {selected && (
          <Box className="h-2 w-2 rounded-full bg-primary-foreground" />
        )}
      </Box>
    </Pressable>
  );
}

export default function ReportScreen() {
  const { nickname } = useLocalSearchParams<{
    id: string;
    nickname?: string;
  }>();
  const [reportType, setReportType] = useState<
    (typeof REPORT_TYPES)[number] | null
  >(null);
  const [reason, setReason] = useState("");
  const headerHeight = useHeaderHeight();

  return (
    <>
      <Stack.Screen options={{ title: `신고 (${nickname || "사용자"})` }} />
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === "ios" ? "padding" : "height"}
        keyboardVerticalOffset={headerHeight}
      >
        <TouchableWithoutFeedback onPress={Keyboard.dismiss} accessible={false}>
          <Box className="flex-1">
            <HStack className="items-center justify-center bg-red-500 px-4 py-2">
              <Text className="text-xs text-white font-semibold">
                신고를 남용할 경우 서비스 이용이 제한될 수 있습니다.
              </Text>
            </HStack>
            <ScrollView
              className="flex-1"
              contentContainerStyle={{ padding: 20, gap: 24 }}
              keyboardShouldPersistTaps="handled"
              showsVerticalScrollIndicator={false}
            >
              <VStack space="lg">
                <VStack space="sm">
                  <Heading size="sm">유형</Heading>
                  <VStack className="overflow-hidden rounded-lg border border-border bg-card">
                    {REPORT_TYPES.map((type, index) => (
                      <ReportTypeRow
                        key={type}
                        label={type}
                        selected={reportType === type}
                        onPress={() => setReportType(type)}
                      />
                    ))}
                  </VStack>
                </VStack>

                <VStack space="sm">
                  <Heading size="sm">증거 사진</Heading>
                  <HStack space="sm" className="w-full">
                    {Array.from(
                      { length: EVIDENCE_PHOTO_SLOT_COUNT },
                      (_, i) => (
                        <Pressable
                          key={i}
                          onPress={() => console.log("사진 업로드")}
                          className="flex-1 aspect-square items-center justify-center rounded-md border border-border bg-secondary"
                        >
                          <Text className="text-2xl">+</Text>
                        </Pressable>
                      ),
                    )}
                  </HStack>
                </VStack>

                <VStack space="sm">
                  <Heading size="sm">신고 사유</Heading>
                  <Input className="h-32 py-2">
                    <InputField
                      type="text"
                      keyboardType="default"
                      inputMode="text"
                      placeholder="자세하게 작성해주시길 바랍니다."
                      className="text-lg"
                      maxLength={1000}
                      multiline
                      textAlignVertical="top"
                      value={reason}
                      onChangeText={setReason}
                    />
                  </Input>
                </VStack>
              </VStack>
            </ScrollView>

            <Box className="bg-background p-6">
              <Button
                variant="destructive"
                size="lg"
                className="w-full py-4"
                isDisabled={!reportType}
                onPress={() => console.log("신고 제출", { reportType, reason })}
              >
                <ButtonText className="text-lg font-semibold">
                  신고하기
                </ButtonText>
              </Button>
            </Box>
          </Box>
        </TouchableWithoutFeedback>
      </KeyboardAvoidingView>
    </>
  );
}
