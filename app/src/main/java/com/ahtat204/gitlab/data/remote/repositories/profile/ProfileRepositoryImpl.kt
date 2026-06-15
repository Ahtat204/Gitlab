package com.ahtat204.gitlab.data.remote.repositories.profile

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

/**
 * Implementation of [ProfileRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Overview
 * - Provides reactive streams of user profile details using Kotlin [Flow].
 * - Uses Apollo’s normalized caching with configurable [FetchPolicy].
 * - Annotated with `@Inject` for dependency injection, ensuring a singleton lifecycle.
 *
 * ## Responsibilities
 * - Fetch a User's profile with various details ,such as profile description , job title,linked accounts,etc...
 * - Handle errors gracefully with logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * - [GetMyProfileQuery]: Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) :
    ProfileRepository {
    /**
     * Retrieves the repository tree for a given project.
     *
     * @param policy The policy to use to fetch data ,ex:from cache first or from network first ,etc... .
     * @return A [Flow] emitting [GetMyProfileQuery.Data] objects, or null if unavailable.
     *
     * ### Behavior
     * - Executes [GetMyProfileQuery].
     * - Uses Apollo’s normalized caching with [FetchPolicy.CacheFirst].
     * - Emits results reactively via Flow.
     * - Logs errors without terminating the stream.
     *
     * ### Example
     * ```kotlin
     * viewModelScope.launch {
     *     profileRepository.getMyProfile(FetchPolicy.CacheFirst)
     *         .collect { myProfile=it.CurrentUser }
     * }
     * ```
     */
    override fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data> {
        return apolloClient.query(GetMyProfileQuery()).fetchPolicy(policy).watch()
            .mapNotNull { it.data }.catch { ex ->
                Log.e("ProjectRepository", ex.cause.toString() + "\n" + ex.stackTrace)
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }
}
