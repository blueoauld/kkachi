import { Tabs } from "expo-router";
import {
  Bell,
  BellOff,
  Ellipsis,
  House,
  MessageCircle,
  Search,
  Settings,
  SlidersHorizontal,
  SquarePen,
  Trophy,
} from "lucide-react-native";
import { useEffect, useRef, useState } from "react";
import { Pressable, type TextInput, useColorScheme, View } from "react-native";

import { Button, ButtonText } from "@/components/ui/button";
import { Heading } from "@/components/ui/heading";
import { Input, InputField } from "@/components/ui/input";
import { Menu, MenuItem, MenuItemLabel } from "@/components/ui/menu";
import {
  Modal,
  ModalBackdrop,
  ModalBody,
  ModalContent,
  ModalFooter,
  ModalHeader,
} from "@/components/ui/modal";

export default function MainLayout() {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === "dark";

  const [showCommentModal, setShowCommentModal] = useState(false);
  const commentInputRef = useRef<TextInput>(null);
  const [chatNotificationsEnabled, setChatNotificationsEnabled] =
    useState(true);

  useEffect(() => {
    if (!showCommentModal) return;

    const timer = setTimeout(() => commentInputRef.current?.focus(), 200);
    return () => clearTimeout(timer);
  }, [showCommentModal]);

  const background = isDark ? "rgb(10 10 10)" : "rgb(255 255 255)";
  const foreground = isDark ? "rgb(255 255 255)" : "rgb(10 10 10)";

  return (
    <>
      <Tabs
        screenOptions={{
          headerShown: true,
          headerStyle: {
            backgroundColor: background,
          },
          headerTintColor: foreground,
          headerShadowVisible: false,
          headerBackButtonDisplayMode: "minimal",
          tabBarStyle: {
            backgroundColor: background,
            borderTopColor: isDark ? "rgb(40 40 40)" : "rgb(200 200 200)",
            paddingTop: 2,
            height: 80,
          },
          sceneStyle: {
            backgroundColor: background,
          },
          tabBarIconStyle: {
            marginTop: 4,
            marginBottom: 2,
          },
          tabBarActiveTintColor: foreground,
          tabBarInactiveTintColor: isDark
            ? "rgb(115 115 115)"
            : "rgb(163 163 163)",
        }}
      >
        <Tabs.Screen
          name="home"
          options={{
            title: "메인",
            tabBarIcon: ({ color, size }) => (
              <House color={color} size={size} />
            ),
            headerLeft: () => (
              <Pressable
                hitSlop={8}
                style={{ paddingHorizontal: 16 }}
                onPress={() => {}}
              >
                <Search color={foreground} size={24} />
              </Pressable>
            ),
            headerRight: () => (
              <View
                style={{
                  flexDirection: "row",
                  alignItems: "center",
                  gap: 16,
                  paddingHorizontal: 16,
                }}
              >
                <Menu
                  placement="bottom right"
                  offset={8}
                  trigger={({ ...triggerProps }) => (
                    <Pressable hitSlop={8} {...triggerProps}>
                      <SlidersHorizontal color={foreground} size={24} />
                    </Pressable>
                  )}
                >
                  <MenuItem key="all" textValue="전체">
                    <MenuItemLabel className="text-lg">전체</MenuItemLabel>
                  </MenuItem>
                  <MenuItem key="male" textValue="남자">
                    <MenuItemLabel className="text-lg">남자</MenuItemLabel>
                  </MenuItem>
                  <MenuItem key="female" textValue="여자">
                    <MenuItemLabel className="text-lg">여자</MenuItemLabel>
                  </MenuItem>
                </Menu>
                <Pressable
                  hitSlop={8}
                  onPress={() => setShowCommentModal(true)}
                >
                  <SquarePen color={foreground} size={24} />
                </Pressable>
              </View>
            ),
          }}
        />
        <Tabs.Screen
          name="chat"
          options={{
            title: "채팅",
            tabBarIcon: ({ color, size }) => (
              <MessageCircle color={color} size={size} />
            ),
            headerRight: () => (
              <Pressable
                hitSlop={8}
                style={{ paddingHorizontal: 16 }}
                onPress={() => setChatNotificationsEnabled((prev) => !prev)}
              >
                {chatNotificationsEnabled ? (
                  <Bell color={foreground} size={24} />
                ) : (
                  <BellOff color={foreground} size={24} />
                )}
              </Pressable>
            ),
          }}
        />
        <Tabs.Screen
          name="ranking"
          options={{
            title: "랭킹",
            tabBarIcon: ({ color, size }) => (
              <Trophy color={color} size={size} />
            ),
          }}
        />
        <Tabs.Screen
          name="setting"
          options={{
            title: "설정",
            tabBarIcon: ({ color, size }) => (
              <Settings color={color} size={size} />
            ),
            headerRight: () => (
              <Menu
                placement="bottom right"
                offset={8}
                trigger={({ ...triggerProps }) => (
                  <Pressable
                    hitSlop={8}
                    style={{ paddingHorizontal: 16 }}
                    {...triggerProps}
                  >
                    <Ellipsis color={foreground} size={24} />
                  </Pressable>
                )}
              >
                <MenuItem
                  key="logout"
                  textValue="로그아웃"
                  onPress={() => console.log("로그아웃")}
                >
                  <MenuItemLabel className="text-lg">로그아웃</MenuItemLabel>
                </MenuItem>
                <MenuItem
                  key="withdraw"
                  textValue="회원탈퇴"
                  onPress={() => console.log("회원탈퇴")}
                >
                  <MenuItemLabel className="text-lg text-destructive">
                    회원탈퇴
                  </MenuItemLabel>
                </MenuItem>
              </Menu>
            ),
          }}
        />
      </Tabs>

      <Modal
        isOpen={showCommentModal}
        onClose={() => setShowCommentModal(false)}
      >
        <ModalBackdrop />
        <ModalContent className="mb-56">
          <ModalHeader>
            <Heading size="lg">코멘트</Heading>
          </ModalHeader>
          <ModalBody>
            <Input>
              <InputField
                ref={commentInputRef}
                placeholder="내용 입력"
                className="text-base"
                maxLength={100}
              />
            </Input>
          </ModalBody>
          <ModalFooter>
            <Button
              variant="outline"
              action="secondary"
              className="flex-1"
              onPress={() => setShowCommentModal(false)}
            >
              <ButtonText className="text-base">닫기</ButtonText>
            </Button>
            <Button className="flex-1" onPress={() => {}}>
              <ButtonText className="text-base">작성</ButtonText>
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </>
  );
}
