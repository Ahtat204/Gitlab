package com.ahtat204.gitlab.data.remote.repositories.profile

import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.data.remote.repositories.mapAndHandleErrors
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

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
     * ### Examples
     * usage in ViewModel
     * ```kotlin
     * viewModelScope.launch {
     *     profileRepository.getMyProfile(FetchPolicy.CacheFirst)
     *         .collect { myProfile=it.CurrentUser }
     * }
     * ```
     * Query Example
     * ```
     *     currentUser {
     *         id
     *         name
     *         username
     *         publicEmail
     *         avatarUrl
     *         webUrl
     *         status {
     *             availability
     *             emoji
     *             message
     *
     *         }
     *         bio
     *         location
     *         github
     *         jobTitle
     *         projectCount
     *         linkedin
     *     }
     * ```
     */

    override fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data> {
        return apolloClient.query(GetMyProfileQuery()).fetchPolicy(policy).watch()
            .mapAndHandleErrors()
    }
}
