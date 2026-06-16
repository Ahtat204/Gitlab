package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.repositories.user.UserRepository
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.exception.CacheMissException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    fun loadUserProjects(userName: String) {
        viewModelScope.launch {
            try {
                userRepository.getUserProjectsByName(userName, FetchPolicy.CacheFirst)
                    .collect { data ->
                    }
            } catch (ex: Exception) {
                if (ex is CacheMissException) {
                    userRepository.getUserProjectsByName(userName, FetchPolicy.NetworkFirst)
                        .collect { data ->
                        }
                }
                if (ex is CancellationException) throw ex
            }
        }
    }
}