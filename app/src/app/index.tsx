import { Box } from "@/components/ui/box";
import { Text } from "@/components/ui/text";
import { Link } from "expo-router";

export default function IndexScreen() {
  return (
    <Box className="flex-1">
      <Link href="/(auth)/login">
        <Text>로그인</Text>
      </Link>
      <Link href="/(auth)/signup">
        <Text>회원가입</Text>
      </Link>
      <Link href="/(profile)/setting">
        <Text>프로필 설정</Text>
      </Link>
    </Box>
  );
}
