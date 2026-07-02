import { FlatList } from "react-native";

import { Box } from "@/components/ui/box";
import { HStack } from "@/components/ui/hstack";
import { Text } from "@/components/ui/text";
import { VStack } from "@/components/ui/vstack";

type PointTransactionType = "EARN" | "SPEND";

type PointHistory = {
  historyId: string;
  type: PointTransactionType;
  amount: number;
  balanceAfter: number;
  description: string;
  createdAt: Date;
};

const EARN_DESCRIPTIONS = [
  "출석 체크 보상",
  "광고 시청 보상",
  "이벤트 참여 보상",
];
const SPEND_DESCRIPTIONS = ["비밀 사진 열람", "메시지 전송"];

const BALANCE = 1200;

const HISTORIES: PointHistory[] = (() => {
  let balance = BALANCE;
  const now = Date.now();

  return Array.from({ length: 30 }, (_, i) => {
    const isEarn = i % 3 !== 0;
    const amount = isEarn ? [50, 100, 300][i % 3] : [30, 80][i % 2];
    const balanceAfter = balance;
    balance = isEarn ? balance - amount : balance + amount;

    return {
      historyId: String(i),
      type: isEarn ? "EARN" : "SPEND",
      amount,
      balanceAfter,
      description: isEarn
        ? EARN_DESCRIPTIONS[i % EARN_DESCRIPTIONS.length]
        : SPEND_DESCRIPTIONS[i % SPEND_DESCRIPTIONS.length],
      createdAt: new Date(now - i * 1000 * 60 * 60 * 7),
    } satisfies PointHistory;
  });
})();

function formatDate(date: Date) {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, "0");
  const d = String(date.getDate()).padStart(2, "0");
  const h = String(date.getHours()).padStart(2, "0");
  const min = String(date.getMinutes()).padStart(2, "0");
  return `${y}.${m}.${d} ${h}:${min}`;
}

function BalanceCard() {
  return (
    <Box className="items-center rounded-2xl border border-border bg-card p-8">
      <Text className="text-sm text-muted-foreground">보유 포인트</Text>
      <HStack className="mt-1 items-end gap-1">
        <Text className="text-4xl font-bold text-foreground">
          {BALANCE.toLocaleString()}
        </Text>
        <Text className="text-lg font-semibold text-muted-foreground">P</Text>
      </HStack>
    </Box>
  );
}

export default function PointsScreen() {
  return (
    <Box className="flex-1">
      <Box className="p-4">
        <BalanceCard />
      </Box>

      <FlatList
        data={HISTORIES}
        keyExtractor={(item) => item.historyId}
        contentContainerStyle={{
          paddingHorizontal: 16,
          paddingBottom: 32,
          gap: 8,
        }}
        renderItem={({ item }) => (
          <HStack className="items-end justify-between">
            <VStack>
              <Text className="text-lg">{item.description}</Text>
              <Text className="text-sm text-muted-foreground">
                {formatDate(item.createdAt)}
              </Text>
            </VStack>
            <VStack className="items-end">
              <Text
                className={`text-lg font-semibold ${
                  item.type === "EARN" ? "text-red-500" : "text-blue-500"
                }`}
              >
                {item.type === "EARN" ? "+" : "-"}
                {item.amount.toLocaleString()}
              </Text>
              <Text className="text-sm text-muted-foreground">
                잔액 {item.balanceAfter.toLocaleString()}
              </Text>
            </VStack>
          </HStack>
        )}
      />
    </Box>
  );
}
