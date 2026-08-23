package com.ahtat204.gitlab.presentation.viewmodels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import com.apollographql.apollo.exception.CacheMissException
import com.apollographql.cache.normalized.FetchPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the user's profile state and data retrieval.
 *
 * This ViewModel acts as an intermediary between the UI layer and the [ProfileRepository],
 * handling data fetching, caching strategies, and state exposure. It utilizes [StateFlow]
 * to provide a thread-safe, observable stream of the current user's profile information.
 *
 * @property graphqlRepository The repository instance responsible for data access,
 * injected via Hilt.
 *
 * @see GetMyProfileQuery
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val graphqlRepository: GraphQlRepository
) : ViewModel() {
    private val profile = MutableStateFlow<GetMyProfileQuery.CurrentUser?>(null)

    /**
     * Exposes the current user's profile as an immutable [kotlinx.coroutines.flow.StateFlow].
     */
    val currentUser = profile.asStateFlow()

    /**
     * Loads the profile information.
     *
     * It attempts a [FetchPolicy.CacheFirst] strategy to minimize latency and data
     * usage. If a [CacheMissException] occurs (meaning no local cache is available),
     * it transparently falls back to [FetchPolicy.NetworkFirst].
     * it fetches the currently authenticated user's profile.
     */
    fun loadProfile() {
            viewModelScope.launch {
            graphqlRepository.getMyProfile().collect { profile.value = it.currentUser }
            }

    }
}