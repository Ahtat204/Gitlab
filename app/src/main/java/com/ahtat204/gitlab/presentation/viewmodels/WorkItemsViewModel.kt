package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetCurrentUserWorkItemsQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias WorkItems = GetCurrentUserWorkItemsQuery.WorkItems?

@HiltViewModel
class WorkItemsViewModel @Inject constructor(private val repository: GraphQlRepository) :
    ViewModel() {
    private val _workItems = MutableStateFlow<WorkItems>(null)
    val workItems: StateFlow<WorkItems> get() = _workItems.asStateFlow()

    fun loadCurrentUserWorkItems() {
        val value = _workItems.value
        val page = value?.pageInfo
        if (value == null) {
            viewModelScope.launch {
                repository.getCurrentUserWorkItems()
                    .collect { _workItems.value = it.currentUser?.workItems }
            }
        } else {

            val endCursor = page?.endCursor //When paginating forwards, the cursor to continue.
            if (page?.hasNextPage == true && endCursor != null)
                viewModelScope.launch {
                    repository.getCurrentUserWorkItems(endCursor)
                        .collect { _workItems.value = it.currentUser?.workItems }
                }
        }
    }
}