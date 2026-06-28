import 'package:app/model/member.dart';

/// 좋아요/비밀 사진/차단 목록 등 활동 목록에 표시되는 회원 항목.
///
/// 메인/검색의 [Member]와 달리 거리·갱신 시각·하트 수는 다루지 않고,
/// 목록 행에 필요한 최소 정보만 가진다.
class MemberActivity {
  const MemberActivity({
    required this.profileUrl,
    required this.nickname,
    required this.gender,
    required this.age,
    required this.comment,
  });

  final String? profileUrl;
  final String nickname;
  final String gender;
  final int age;
  final String comment;
}

extension MemberActivityNavigation on MemberActivity {
  /// 회원 상세 화면([MemberDetailView])으로 이동할 때 넘길 [Member]로 변환한다.
  ///
  /// 활동 목록이 다루지 않는 갱신 시각·거리·하트 수는 중립값으로 채운다.
  /// (상세 화면은 현재 mock 데이터를 사용하므로 이 값들을 읽지 않는다.)
  Member toMember() => Member(
    profileUrl: profileUrl,
    nickname: nickname,
    updatedAt: '',
    gender: gender,
    age: age,
    distance: null,
    comment: comment,
    hearts: 0,
  );
}
