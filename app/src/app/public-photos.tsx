import { Trash2 } from "lucide-react-native";
import { FlatList } from "react-native";

import { Avatar, AvatarFallbackText } from "@/components/ui/avatar";
import { Box } from "@/components/ui/box";
import { HStack } from "@/components/ui/hstack";
import { Icon } from "@/components/ui/icon";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

const DATA = Array.from({ length: 100 }, (_, i) => ({
  id: String(i),
  title: `제목 ${i + 1}`,
}));

export default function PublicPhotosScreen() {
  return (
    <Box className="flex-1">
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
              <Text numberOfLines={1} className="text-sm text-muted-foreground">
                안녕하세요
              </Text>
            </VStack>
            <Pressable
              hitSlop={8}
              className="h-10 w-10 items-center justify-center rounded-full bg-red-500"
            >
              <Icon as={Trash2} size="sm" className="text-white" />
            </Pressable>
          </HStack>
        )}
      />
    </Box>
  );
}
