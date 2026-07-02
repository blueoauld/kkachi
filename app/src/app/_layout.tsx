import { Stack } from "expo-router";
import { useColorScheme } from "react-native";

import { GluestackUIProvider } from "@/components/ui/gluestack-ui-provider";
import "@/global.css";

export default function RootLayout() {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === "dark";

  return (
    <GluestackUIProvider mode="system">
      <Stack
        screenOptions={{
          headerShown: true,
          headerStyle: {
            backgroundColor: isDark ? "rgb(10 10 10)" : "rgb(255 255 255)",
          },
          headerTintColor: isDark ? "rgb(255 255 255)" : "rgb(10 10 10)",
          headerShadowVisible: false,
          headerBackButtonDisplayMode: "minimal",
          contentStyle: {
            backgroundColor: isDark ? "rgb(10 10 10)" : "rgb(255 255 255)",
          },
        }}
      >
        <Stack.Screen name="index" options={{ title: "인덱스" }} />
        <Stack.Screen name="(auth)/login" options={{ title: "로그인" }} />
        <Stack.Screen name="(auth)/signup" options={{ title: "회원가입" }} />
        <Stack.Screen
          name="(profile)/setting"
          options={{ title: "프로필 설정" }}
        />
        <Stack.Screen name="search" options={{ title: "검색" }} />
        <Stack.Screen name="liked" options={{ title: "누른 좋아요 목록" }} />
        <Stack.Screen
          name="public-photos"
          options={{ title: "공개한 비밀 사진 목록" }}
        />
        <Stack.Screen name="blocked" options={{ title: "차단 목록" }} />
        <Stack.Screen
          name="received-likes"
          options={{ title: "받은 좋아요 목록" }}
        />
        <Stack.Screen
          name="received-photos"
          options={{ title: "공개된 비밀 사진 목록" }}
        />
        <Stack.Screen name="(main)" options={{ headerShown: false }} />
      </Stack>
    </GluestackUIProvider>
  );
}
