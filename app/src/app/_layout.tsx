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
        <Stack.Screen name="(profile)/setting" options={{ title: "프로필 설정" }} />
      </Stack>
    </GluestackUIProvider>
  );
}
