package com.blueoauld.server.global.response

data class CursorResponse<T>(

    val items: List<T>,
    val nextCursor: Long?,
    val hasNext: Boolean,
) {

    companion object {
        fun <E, R : Any> of(
            fetched: List<E>,
            size: Int,
            idExtractor: (E) -> Long,
            mapper: (E) -> R?,
        ): CursorResponse<R> {
            val hasNext = fetched.size > size
            val page = if (hasNext) fetched.take(size) else fetched

            return CursorResponse(
                items = page.mapNotNull(mapper),
                nextCursor = if (hasNext) idExtractor(page.last()) else null,
                hasNext = hasNext,
            )
        }
    }
}
