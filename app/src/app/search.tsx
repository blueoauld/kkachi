import { useRouter } from "expo-router";
import { Search } from "lucide-react-native";
import { useMemo, useState } from "react";
import { FlatList, Pressable } from "react-native";

import { Avatar, AvatarFallbackText } from "@/components/ui/avatar";
import { Box } from "@/components/ui/box";
import { HStack } from "@/components/ui/hstack";
import { Input, InputField, InputIcon, InputSlot } from "@/components/ui/input";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

const DATA = Array.from({ length: 100 }, (_, i) => ({
  id: String(i),
  title: `제목 ${i + 1}`,
}));

export default function SearchScreen() {
  const router = useRouter();
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
                <Text className="text-lg font-semibold">{item.title}</Text>
                <Text className="text-sm text-muted-foreground">
                  여자 · 20살 · ♥ 100
                </Text>
                <Text
                  numberOfLines={2}
                  className="text-sm text-muted-foreground"
                >
                  안녕하세요
                </Text>
              </VStack>
            </HStack>
          </Pressable>
        )}
      />
    </Box>
  );
}
