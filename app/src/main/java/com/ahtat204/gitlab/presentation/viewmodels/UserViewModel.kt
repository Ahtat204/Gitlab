package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetMemberProfileQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias User = GetMemberProfileQuery.User?

class UserViewModel @Inject constructor(
    private val repository: GraphQlRepository
) : ViewModel() {
    private val profile = MutableStateFlow<User>(null)

    /**
     * Exposes the user's profile as an immutable [kotlinx.coroutines.flow.StateFlow].
     */
    val member get() = profile.asStateFlow()

    fun loadProfile(member: String) {
        viewModelScope.launch {
            repository.getMemberProfile(member).collect { profile.value = it.user }
        }

    }
}