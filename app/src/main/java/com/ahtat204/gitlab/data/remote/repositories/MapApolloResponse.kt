package com.ahtat204.gitlab.data.remote.repositories

import android.media.VolumeShaper
import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import okio.IOException

/**
 * Processes an Apollo GraphQL [Flow] response, providing unified error handling,
 * data extraction, and automatic filtering of null values.
 *
 * ## Features
 * - **Exception Unwrapping**: Automatically detects and throws [ApolloResponse.exception]
 *   to propagate network or transport-level failures to the [catch] block.
 * - **GraphQL Error Handling**: Inspects [ApolloResponse.hasErrors] and throws an [Exception]
 *   if the server returns business-logic errors, ensuring they are not ignored.
 * - **Unified Error Logging**: Centralizes error handling via a [catch] block, distinguishing
 *   between recoverable network issues ([IOException]), coroutine lifecycle events
 *   ([CancellationException]), and unexpected system failures.
 * - **Safe Data Emission**: Sanitizes the stream by returning only valid [D] (data)
 *   and filtering out nulls via [mapNotNull].
 *
 * ## Usage
 * ```kotlin
 * override suspend fun getProjects(): Flow<ProjectQuery.Data> =
 *     apolloClient.query(ProjectQuery())
 *         .watch()
 *         .mapAndHandleErrors()
 * ```
 *
 * @param D The type of the GraphQL operation data (e.g., `Query.Data` or `Mutation.Data`).
 * @return A [Flow] emitting the raw data [D], with all errors intercepted and logged.
 *
 * @throws Exception Propagates exceptions caught during stream collection,
 *                   excluding [CancellationException] which is re-thrown to honor coroutine lifecycle.
 * @author Lahcen AHTAT
 */

fun <D : Query.Data> Flow<ApolloResponse<D>>.mapAndHandleErrors(): Flow<D> {
    return this.map { response ->
        response.exception?.cause?.let {
            throw it
        }
        response.data
    }.catch { ex ->
        when (ex) {
            is IOException ->logger(message = ex.message)
            is CancellationException -> throw ex
            else -> logger(message = null)
        }
    }.mapNotNull { it }
}