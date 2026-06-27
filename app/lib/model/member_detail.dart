class MemberDetail {
  const MemberDetail({
    required this.publicImageUrls,
    required this.privateImageCount,
    required this.nickname,
    required this.updatedAt,
    required this.gender,
    required this.age,
    required this.distance,
    required this.bio,
    required this.hearts,
  });

  final List<String> publicImageUrls;
  final int privateImageCount;
  final String nickname;
  final String updatedAt;
  final String gender;
  final int age;
  final double? distance;
  final String bio;
  final int hearts;
}
