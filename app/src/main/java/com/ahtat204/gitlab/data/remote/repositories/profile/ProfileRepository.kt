package com.ahtat204.gitlab.data.remote.repositories.profile

import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.apollographql.apollo.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
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
    fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data>
}