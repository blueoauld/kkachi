import { Search } from "lucide-react-native";
import { useMemo, useState } from "react";
import { FlatList } from "react-native";

import { Avatar, AvatarFallbackText } from "@/components/ui/avatar";
import { Box } from "@/components/ui/box";
import { HStack } from "@/components/ui/hstack";
import { Input, InputField, InputIcon, InputSlot } from "@/components/ui/input";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";
import { Pressable } from "@/components/ui/pressable";

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

export default function ChatSearchScreen() {
  const [query, setQuery] = useState("");

  const results = useMemo(
    () => DATA.filter((item) => item.title.includes(query)),
    [query],
  );

  return (
    <Box className="flex-1">
      <Box className="mx-4 my-2">
        <Input>
          <InputSlot>
            <InputIcon as={Search} />
          </InputSlot>
          <InputField
            placeholder="닉네임 (2자 이상)"
            value={query}
            onChangeText={setQuery}
            className="text-base"
          />
        </Input>
      </Box>
      <FlatList
        data={results}
        keyExtractor={(item) => item.id}
        contentContainerStyle={{
          paddingHorizontal: 16,
          paddingTop: 8,
          paddingBottom: 16,
          gap: 8,
        }}
        renderItem={({ item }) => (
          <Pressable onPress={() => console.log("채팅방 열기", item.id)}>
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
          </Pressable>
        )}
      />
    </Box>
  );
}
