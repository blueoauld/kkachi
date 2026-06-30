import 'package:flutter_riverpod/flutter_riverpod.dart';

/// 비동기 데이터를 다루는 ViewModel의 베이스 클래스.
///
/// SwiftUI의 `ObservableObject` ViewModel에 대응한다.
/// Riverpod에서는 로딩/에러/데이터 상태를 [AsyncValue]가 직접 표현하므로
/// `isLoading`/`error` 필드를 수동으로 관리하지 않는다. View는 [state]
/// 하나만 구독하여 `state.when(loading:..., error:..., data:...)`으로 분기한다.
///
/// [build]에서 초기 데이터를 로드하고, 상태를 바꾸는 동작은 [mutate]로 감싼다.
///
/// ```dart
/// class HomeViewModel extends BaseAsyncViewModel<List<Item>> {
///   ItemRepository get _repo => ref.read(itemRepositoryProvider);
///
///   @override
///   Future<List<Item>> build() => _repo.fetchItems();
///
///   Future<void> refresh() => mutate(() => _repo.fetchItems());
/// }
///
/// final homeViewModelProvider =
///     AsyncNotifierProvider<HomeViewModel, List<Item>>(HomeViewModel.new);
/// ```
abstract class BaseAsyncViewModel<T> extends AsyncNotifier<T> {
  /// 상태를 변경하는 비동기 작업을 로딩 표시 + 에러 캡처로 안전하게 실행한다.
  ///
  /// 작업 중에는 [state]가 `AsyncLoading`, 성공하면 `AsyncData`,
  /// 예외가 발생하면 `AsyncError`로 자동 전환된다.
  Future<void> mutate(Future<T> Function() action) async {
    state = const AsyncValue.loading();
    state = await AsyncValue.guard(action);
  }
}
