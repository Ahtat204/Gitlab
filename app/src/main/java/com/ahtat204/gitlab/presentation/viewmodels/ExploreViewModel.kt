package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.SearchUserByNameQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias User = SearchUserByNameQuery.Data?

@HiltViewModel
class ExploreViewModel @Inject constructor(private val repository: ProjectRepository) :
    ViewModel() {
    private val _user = MutableStateFlow<User>(null)
    val user: StateFlow<User> get() = _user.asStateFlow()
    fun loadUserByName(userName: String) {
        viewModelScope.launch {
            repository.searchUserByName(userName).collect { _user.value = it }
        }
    }
}