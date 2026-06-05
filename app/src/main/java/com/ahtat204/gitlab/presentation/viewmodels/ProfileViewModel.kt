package com.ahtat204.gitlab.presentation.viewmodels

import android.net.http.NetworkException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.data.repositories.profile.ProfileRepository
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.exception.CacheMissException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
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
 * @property profileRepository The repository instance responsible for data access,
 * injected via Hilt.
 *
 * @see GetMyProfileQuery
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
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
     *
     * @param userName Optional username to fetch a specific profile. If null,
     * it fetches the currently authenticated user's profile.
     */
  //  @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun loadProfile(userName: String? = null) {
        if (userName == null) {
            viewModelScope.launch {
                try {
                    profileRepository.getMyProfile(FetchPolicy.CacheFirst)
                        .collect { profile.value = it.currentUser }
                } catch (e: Exception) {
                    if (e is CacheMissException) {
                        profileRepository.getMyProfile(FetchPolicy.NetworkFirst)
                            .collect { profile.value = it.currentUser }
                    }
                    if (e is CancellationException) throw e
            //        if(e is NetworkException) throw e
                }
            }
        }
    }
}