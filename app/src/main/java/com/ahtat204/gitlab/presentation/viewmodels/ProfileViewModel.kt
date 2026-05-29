package com.ahtat204.gitlab.presentation.viewmodels

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

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {
    private val profile=MutableStateFlow<GetMyProfileQuery.CurrentUser?>(null)
    val currentUser get() = profile.asStateFlow()
    fun loadProfile(userName:String?=null) {
        if(userName == null){
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
                }
            }
        }

    }
}