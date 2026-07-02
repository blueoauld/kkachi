import { useRouter } from "expo-router";
import { useState } from "react";
import { FlatList } from "react-native";

import { Avatar, AvatarFallbackText } from "@/components/ui/avatar";
import { Box } from "@/components/ui/box";
import { HStack } from "@/components/ui/hstack";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

const TABS = [
  { value: "all", label: "전체" },
  { value: "male", label: "남자" },
  { value: "female", label: "여자" },
] as const;

const DATA = Array.from({ length: 100 }, (_, i) => ({
  id: String(i),
  title: `제목 ${i + 1}`,
}));

export default function RankingScreen() {
  const router = useRouter();
  const [tab, setTab] = useState("all");

  return (
    <Box className="flex-1">
      <HStack className="mx-4 my-2 rounded-lg bg-muted p-1">
        {TABS.map(({ value, label }) => {
          const selected = tab === value;
          return (
            <Pressable
              key={value}
              onPress={() => setTab(value)}
              className={`flex-1 items-center rounded-md py-1.5 ${
                selected ? "bg-background" : ""
              }`}
            >
              <Text
                className={`text-sm font-medium ${
                  selected ? "text-foreground" : "text-muted-foreground"
                }`}
              >
                {label}
              </Text>
            </Pressable>
          );
        })}
      </HStack>
      <HStack className="items-center bg-muted px-4 py-2 justify-center">
        <Text className="text-xs text-muted-foreground font-semibold">
          좋아요가 많은 순서로 정렬됩니다.
        </Text>
      </HStack>
      <FlatList
        data={DATA}
        keyExtractor={(item) => item.id}
        contentContainerStyle={{
          paddingHorizontal: 16,
          paddingTop: 8,
          paddingBottom: 16,
          gap: 4,
        }}
        renderItem={({ item }) => (
          <Pressable onPress={() => router.push(`/member/${item.id}`)}>
            <HStack className="gap-3 items-center">
              <Avatar className="h-16 w-16">
                <AvatarFallbackText className="text-base">
                  {item.title}
                </AvatarFallbackText>
              </Avatar>
              <VStack className="flex-1">
                <HStack className="items-end justify-between">
                  <Text className="text-lg font-semibold">{item.title}</Text>
                  <Text className="text-xs text-muted-foreground">방금전</Text>
                </HStack>
                <Text className="text-sm text-muted-foreground">
                  여자 · 20살 · ♥ 100
                </Text>
                <HStack className="items-end justify-between gap-2">
                  <Text
                    numberOfLines={2}
                    className="flex-1 text-sm text-muted-foreground"
                  >
                    안녕하세요
                  </Text>
                  <Text className="text-xs text-muted-foreground">0.3km</Text>
                </HStack>
              </VStack>
            </HStack>
          </Pressable>
        )}
      />
    </Box>
  );
}
