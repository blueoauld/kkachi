import { Image } from "expo-image";
import { Stack, useLocalSearchParams, useRouter } from "expo-router";
import {
  Ban,
  Ellipsis,
  Heart,
  Image as ImageIcon,
  MessageCircle,
  Star,
  User,
} from "lucide-react-native";
import { useState } from "react";
import { Dimensions, ScrollView, useColorScheme } from "react-native";
import Carousel from "react-native-reanimated-carousel";

import { Box } from "@/components/ui/box";
import { Heading } from "@/components/ui/heading";
import { HStack } from "@/components/ui/hstack";
import { Icon } from "@/components/ui/icon";
import { Menu, MenuItem, MenuItemLabel } from "@/components/ui/menu";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

const SCREEN_WIDTH = Dimensions.get("window").width;

type GenderLabel = "남자" | "여자";

type Member = {
  nickname: string;
  genderLabel: GenderLabel;
  age: number;
  heartCount: number;
  distanceLabel: string;
  updatedAtLabel: string;
  comment: string;
  bio: string;
  imageUrls: string[];
  secretPhotoCount: number;
};

const MEMBERS: Member[] = [
  {
    nickname: "민지",
    genderLabel: "여자",
    age: 24,
    heartCount: 128,
    distanceLabel: "0.3km",
    updatedAtLabel: "방금전",
    comment: "오늘 저녁 같이 드실 분 구해요 :)",
    bio: "여행이랑 맛집 탐방 좋아해요. 편하게 말 걸어주세요!",
    imageUrls: [
      "https://picsum.photos/seed/kkachi-1-1/800/1000",
      "https://picsum.photos/seed/kkachi-1-2/800/1000",
      "https://picsum.photos/seed/kkachi-1-3/800/1000",
    ],
    secretPhotoCount: 3,
  },
  {
    nickname: "서준",
    genderLabel: "남자",
    age: 27,
    heartCount: 96,
    distanceLabel: "1.2km",
    updatedAtLabel: "5분 전",
    comment:
      "운동 좋아하시는 분 환영합니다 운동 좋아하시는 분 환영합니다 운동 좋아하시는 분 환영합니다 운동 좋아하시는 분 환영합니다 운동 좋아하시는 분 환영합니다 운동 좋아하시는 분 환영합니다 운동 좋아하시는 분 환영합니다",
    bio: "주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다.주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다. 주말엔 등산, 평일엔 헬스장 다녀요. 건강한 라이프스타일 지향합니다.",
    imageUrls: [
      // "https://picsum.photos/seed/kkachi-2-1/800/1000",
      // "https://picsum.photos/seed/kkachi-2-2/800/1000",
    ],
    secretPhotoCount: 0,
  },
  {
    nickname: "하은",
    genderLabel: "여자",
    age: 22,
    heartCount: 214,
    distanceLabel: "2.8km",
    updatedAtLabel: "30분 전",
    comment: "먼저 말 걸어주면 좋아요~",
    bio: "그림 그리는 거 좋아하는 대학생입니다. 잔잔한 카페 데이트 좋아해요.",
    imageUrls: [
      // "https://picsum.photos/seed/kkachi-3-1/800/1000",
      // "https://picsum.photos/seed/kkachi-3-2/800/1000",
      // "https://picsum.photos/seed/kkachi-3-3/800/1000",
      // "https://picsum.photos/seed/kkachi-3-4/800/1000",
    ],
    secretPhotoCount: 5,
  },
  {
    nickname: "도윤",
    genderLabel: "남자",
    age: 29,
    heartCount: 61,
    distanceLabel: "4.5km",
    updatedAtLabel: "2시간 전",
    comment: "인연이 되면 좋겠네요",
    bio: "IT 회사 다니는 직장인입니다. 영화, 넷플릭스 좋아해요.",
    imageUrls: [
      // "https://picsum.photos/seed/kkachi-4-1/800/1000",
      // "https://picsum.photos/seed/kkachi-4-2/800/1000",
    ],
    secretPhotoCount: 1,
  },
];

function CarouselDots({
  total,
  activeIndex,
}: {
  total: number;
  activeIndex: number;
}) {
  if (total <= 1) return null;

  return (
    <HStack className="absolute bottom-3 w-full justify-center gap-1.5">
      {Array.from({ length: total }, (_, i) => (
        <Box
          key={i}
          className={`h-1.5 w-1.5 rounded-full ${
            i === activeIndex ? "bg-white" : "bg-white/40"
          }`}
        />
      ))}
    </HStack>
  );
}

function ActionBarButton({
  icon,
  label,
  active,
  activeClassName,
  badgeCount,
  onPress,
}: {
  icon: typeof Star;
  label: string;
  active: boolean;
  activeClassName: string;
  badgeCount?: number;
  onPress: () => void;
}) {
  const colorClassName = active ? activeClassName : "text-muted-foreground";

  return (
    <Pressable onPress={onPress} className="flex-1 items-center">
      <Box className="relative">
        <Icon as={icon} size="lg" className={`${colorClassName} w-8 h-8`} />
        {badgeCount !== undefined && (
          <Box className="absolute -right-1 -top-1 h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-1">
            <Text className="text-[10px] font-bold leading-none text-white">
              {badgeCount}
            </Text>
          </Box>
        )}
      </Box>
    </Pressable>
  );
}

function ActionBar({
  favorited,
  hearted,
  blocked,
  secretPhotoCount,
  onToggleFavorite,
  onToggleHearted,
  onChat,
  onSecretPhoto,
  onToggleBlocked,
}: {
  favorited: boolean;
  hearted: boolean;
  blocked: boolean;
  secretPhotoCount: number;
  onToggleFavorite: () => void;
  onToggleHearted: () => void;
  onChat: () => void;
  onSecretPhoto: () => void;
  onToggleBlocked: () => void;
}) {
  const colorScheme = useColorScheme();
  const isDark = colorScheme === "dark";

  return (
    <HStack
      className="items-center px-2"
      style={{
        backgroundColor: isDark ? "rgb(10 10 10)" : "rgb(255 255 255)",
        borderTopWidth: 0.3,
        borderTopColor: isDark ? "rgb(40 40 40)" : "rgb(200 200 200)",
        paddingBottom: 16,
        height: 80,
      }}
    >
      <ActionBarButton
        icon={Star}
        label="즐겨찾기"
        active={favorited}
        activeClassName="text-amber-500"
        onPress={onToggleFavorite}
      />
      <ActionBarButton
        icon={Heart}
        label="좋아요"
        active={hearted}
        activeClassName="text-red-500"
        onPress={onToggleHearted}
      />
      <ActionBarButton
        icon={MessageCircle}
        label="채팅"
        active={false}
        activeClassName=""
        onPress={onChat}
      />
      <ActionBarButton
        icon={ImageIcon}
        label="비밀사진"
        active={false}
        activeClassName=""
        badgeCount={secretPhotoCount}
        onPress={onSecretPhoto}
      />
      <ActionBarButton
        icon={Ban}
        label="차단"
        active={blocked}
        activeClassName="text-destructive"
        onPress={onToggleBlocked}
      />
    </HStack>
  );
}

export default function MemberDetailScreen() {
  const router = useRouter();
  const { id } = useLocalSearchParams<{ id: string }>();
  const member = MEMBERS[Math.abs(Number(id) || 0) % MEMBERS.length];
  const [activeIndex, setActiveIndex] = useState(0);
  const [favorited, setFavorited] = useState(false);
  const [hearted, setHearted] = useState(false);
  const [blocked, setBlocked] = useState(false);
  const colorScheme = useColorScheme();
  const isDark = colorScheme === "dark";
  const foreground = isDark ? "rgb(255 255 255)" : "rgb(10 10 10)";

  return (
    <>
      <Stack.Screen
        options={{
          title: "프로필",
          headerRight: () => (
            <Menu
              placement="bottom right"
              offset={8}
              trigger={({ ...triggerProps }) => (
                <Pressable hitSlop={8} {...triggerProps}>
                  <Ellipsis color={foreground} size={24} />
                </Pressable>
              )}
            >
              <MenuItem
                key="reveal-secret-photo"
                textValue="비밀 사진 공개"
                onPress={() => console.log("비밀 사진 공개")}
              >
                <MenuItemLabel className="text-lg">
                  비밀 사진 공개
                </MenuItemLabel>
              </MenuItem>
              <MenuItem
                key="report"
                textValue="신고하기"
                onPress={() =>
                  router.push(`/report/${id}?nickname=${member.nickname}`)
                }
              >
                <MenuItemLabel className="text-lg text-destructive">
                  신고하기
                </MenuItemLabel>
              </MenuItem>
            </Menu>
          ),
        }}
      />
      <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
        <Box>
          {member.imageUrls.length > 0 ? (
            <>
              <Carousel
                width={SCREEN_WIDTH}
                height={SCREEN_WIDTH}
                data={member.imageUrls}
                onSnapToItem={setActiveIndex}
                renderItem={({ item }) => (
                  <Image
                    source={{ uri: item }}
                    style={{ width: "100%", height: "100%" }}
                    contentFit="cover"
                  />
                )}
              />
              <CarouselDots
                total={member.imageUrls.length}
                activeIndex={activeIndex}
              />
            </>
          ) : (
            <Box
              style={{ width: SCREEN_WIDTH, height: SCREEN_WIDTH }}
              className="items-center justify-center bg-muted"
            >
              <Icon as={User} className="text-muted-foreground w-16 h-16" />
            </Box>
          )}
        </Box>

        <VStack className="gap-4 px-4 py-4">
          <VStack className="gap-1">
            <HStack className="items-center justify-between">
              <Text className="text-xl font-bold text-foreground">
                {member.nickname}
              </Text>
              <Text className="text-xs text-muted-foreground">
                {member.updatedAtLabel}
              </Text>
            </HStack>
            <HStack className="items-center justify-between">
              <Text className="text-sm text-muted-foreground">
                {member.genderLabel} · {member.age}살 · ♥ {member.heartCount}
              </Text>
              <Text className="text-xs text-muted-foreground">
                {member.distanceLabel}
              </Text>
            </HStack>
          </VStack>

          <VStack className="gap-1.5">
            <Heading size="sm">코멘트</Heading>
            <Box className="rounded-xl bg-muted p-4">
              <Text className="text-base text-foreground">
                {member.comment}
              </Text>
            </Box>
          </VStack>

          <VStack className="gap-1.5">
            <Heading size="sm">자기소개</Heading>
            <Box className="rounded-xl bg-muted p-4">
              <Text className="text-base leading-6 text-foreground">
                {member.bio}
              </Text>
            </Box>
          </VStack>
        </VStack>
      </ScrollView>
      <ActionBar
        favorited={favorited}
        hearted={hearted}
        blocked={blocked}
        secretPhotoCount={member.secretPhotoCount}
        onToggleFavorite={() => setFavorited((v) => !v)}
        onToggleHearted={() => setHearted((v) => !v)}
        onChat={() => console.log("채팅 요청")}
        onSecretPhoto={() => console.log("비밀사진 요청")}
        onToggleBlocked={() => setBlocked((v) => !v)}
      />
    </>
  );
}
