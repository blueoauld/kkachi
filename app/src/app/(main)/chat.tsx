import { useState } from "react";
import { FlatList, Pressable } from "react-native";

import { Avatar, AvatarFallbackText } from "@/components/ui/avatar";
import { Box } from "@/components/ui/box";
import { HStack } from "@/components/ui/hstack";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

const TABS = [
  { value: "all", label: "전체" },
  { value: "unread", label: "안읽음" },
] as const;

const DATA = Array.from({ length: 100 }, (_, i) => ({
  id: String(i),
  title: `제목 ${i + 1}`,
  unread: (i * 7) % 150,
}));

function UnreadBadge({ count }: { count: number }) {
  if (count <= 0) return null;

  return (
    <Box className="h-5 min-w-5 items-center justify-center rounded-full bg-red-500 px-1.5">
      <Text className="text-xs font-semibold text-white">
        {count > 99 ? "99+" : count}
      </Text>
    </Box>
  );
}

export default function ChatScreen() {
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
      <FlatList
        data={DATA}
        keyExtractor={(item) => item.id}
        contentContainerStyle={{
          paddingHorizontal: 16,
          paddingTop: 8,
          paddingBottom: 16,
          gap: 8,
        }}
        renderItem={({ item }) => (
          <HStack className="gap-3 items-center">
            <Avatar className="h-16 w-16">
              <AvatarFallbackText className="text-base">
                {item.title}
              </AvatarFallbackText>
            </Avatar>
            <VStack className="flex-1">
              <HStack className="items-center justify-between">
                <Text className="text-lg font-semibold">{item.title}</Text>
                <Text className="text-xs text-muted-foreground">
                  오전 11:30
                </Text>
              </HStack>
              <HStack className="items-center justify-between gap-2">
                <Text
                  numberOfLines={2}
                  className="flex-1 text-sm text-muted-foreground"
                >
                  안녕하세요
                </Text>
                <UnreadBadge count={item.unread} />
              </HStack>
            </VStack>
          </HStack>
        )}
      />
    </Box>
  );
}
