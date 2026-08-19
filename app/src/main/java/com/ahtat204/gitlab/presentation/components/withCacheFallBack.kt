package com.ahtat204.gitlab.presentation.components

import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.apollographql.apollo.exception.CacheMissException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll

/**
 * this extension is used to avoid try-catch mess on the [CacheMissException]  inside the ViewModel.
 */
fun <T> Flow<T>.withCacheFallback(
    fallback: suspend () -> Flow<T>
): Flow<T> = this.catch { e ->
    if (e is CacheMissException) {
        emitAll(fallback())
    } else {
        throw e
    }
}
