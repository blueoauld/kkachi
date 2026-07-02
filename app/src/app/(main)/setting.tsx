import { useRouter } from "expo-router";
import {
  Ban,
  Bug,
  CalendarCheck,
  Coins,
  Eye,
  FileText,
  Gift,
  Heart,
  HeartHandshake,
  type LucideIcon,
  MessageCircleQuestion,
  ShieldCheck,
  Unlock,
  User,
} from "lucide-react-native";
import { ScrollView } from "react-native";

import { Box } from "@/components/ui/box";
import { Icon } from "@/components/ui/icon";
import { Pressable } from "@/components/ui/pressable";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

type SettingRow = {
  icon: LucideIcon;
  label: string;
  onPress?: () => void;
};

type SettingGroup = {
  title: string;
  rows: SettingRow[];
};

function SettingItem({
  icon,
  label,
  onPress,
  isLast,
}: SettingRow & { isLast: boolean }) {
  return (
    <Pressable
      onPress={onPress}
      className={`flex-row items-center gap-3 px-4 py-3 ${
        isLast ? "" : "border-border"
      }`}
    >
      <Box className="h-9 w-9 items-center justify-center rounded-xl bg-secondary">
        <Icon as={icon} size="sm" className="text-foreground" />
      </Box>
      <Text className="flex-1 text-base text-foreground">{label}</Text>
    </Pressable>
  );
}

function SettingSection({ title, rows }: SettingGroup) {
  return (
    <VStack space="sm">
      <Text className="ml-1 text-xs font-medium text-muted-foreground">
        {title}
      </Text>
      <VStack className="overflow-hidden rounded-xl border border-border bg-card">
        {rows.map((row, index) => (
          <SettingItem
            key={row.label}
            {...row}
            isLast={index === rows.length - 1}
          />
        ))}
      </VStack>
    </VStack>
  );
}

export default function SettingScreen() {
  const router = useRouter();

  const groups: SettingGroup[] = [
    {
      title: "내 정보",
      rows: [
        {
          icon: User,
          label: "내 프로필",
          onPress: () => router.push("/(profile)/my"),
        },
      ],
    },
    {
      title: "내 활동",
      rows: [
        {
          icon: Heart,
          label: "누른 좋아요 목록",
          onPress: () => router.push("/liked"),
        },
        {
          icon: Unlock,
          label: "공개한 비밀 사진 목록",
          onPress: () => router.push("/public-photos"),
        },
        {
          icon: Ban,
          label: "차단 목록",
          onPress: () => router.push("/blocked"),
        },
      ],
    },
    {
      title: "받은 관심",
      rows: [
        {
          icon: HeartHandshake,
          label: "받은 좋아요 목록",
          onPress: () => router.push("/received-likes"),
        },
        {
          icon: Eye,
          label: "공개된 비밀 사진 목록",
          onPress: () => router.push("/received-photos"),
        },
      ],
    },
    {
      title: "포인트",
      rows: [
        {
          icon: Coins,
          label: "포인트 내역",
          onPress: () => router.push("/points"),
        },
        { icon: CalendarCheck, label: "출석 체크" },
        { icon: Gift, label: "광고 보상" },
      ],
    },
    {
      title: "고객지원",
      rows: [
        { icon: MessageCircleQuestion, label: "문의사항" },
        { icon: Bug, label: "버그 제보" },
        { icon: FileText, label: "서비스 이용약관" },
        { icon: ShieldCheck, label: "개인정보 취급방침" },
      ],
    },
  ];

  return (
    <ScrollView
      className="flex-1"
      contentContainerStyle={{ padding: 16, gap: 24 }}
    >
      {groups.map((group) => (
        <SettingSection key={group.title} {...group} />
      ))}
    </ScrollView>
  );
}
