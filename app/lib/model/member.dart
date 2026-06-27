class Member {
  const Member({
    required this.profileUrl,
    required this.nickname,
    required this.updatedAt,
    required this.gender,
    required this.age,
    required this.distance,
    required this.comment,
    required this.hearts,
  });

  final String? profileUrl;
  final String nickname;
  final String updatedAt;
  final String gender;
  final int age;
  final double? distance;
  final String comment;
  final int hearts;
}
