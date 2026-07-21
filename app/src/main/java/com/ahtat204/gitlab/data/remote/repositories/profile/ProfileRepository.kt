package com.ahtat204.gitlab.data.remote.repositories.profile

import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.apollographql.cache.normalized.FetchPolicy
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract defining access boundaries for the user's profile data.
 *
 * Provides reactive streams to read and observe identity and account configuration rules
 * for the currently authenticated worker or account.
 *
 * @author Lahcen AHTAT
 */
interface ProfileRepository {
    /**
     * Retrieves and monitors the authenticated user's profile details.
     *
     * The returned stream updates reactively whenever underlying changes hit the data layer,
     * restricted by the behavior of the requested retrieval policy.
     *
     * @param policy The data routing strategy ([FetchPolicy]) dictating whether to prioritize
     * local cache snapshots, enforce fresh remote network requests, or combine both layers.
     * @return A reactive cold stream emitting the authenticated user's profile data structure.
     * @throws kotlinx.coroutines.CancellationException if the collection coroutine scope is cancelled.
     */
    fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data>
}
