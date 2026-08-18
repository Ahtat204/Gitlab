package com.ahtat204.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetUserIssuesQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Issues = GetUserIssuesQuery.Data?

@HiltViewModel
class UserIssuesVM @Inject internal constructor(private val repository: GraphQlRepository) :
    ViewModel() {
    private val _issues = MutableStateFlow<Issues>(null)
    val issues: StateFlow<Issues> = _issues.asStateFlow()
    fun loaCurrentUserIssues() {
        val value = _issues.value
        if (value == null) {
            viewModelScope.launch {
                repository.getUserIssues().collect { _issues.value = it }
            }
        } else {
            val projects = value.projects
            val projectPage = projects?.pageInfo
            val projectHasNextPage = projectPage?.hasNextPage
            val projectCursor = projectPage?.endCursor
            val nodes = projects?.nodes
            nodes?.forEach { node ->
                val issuesPage = node?.issues?.pageInfo
                val hasNextPage = issuesPage?.hasNextPage
                val issuesCursor = issuesPage?.endCursor
                if (projectHasNextPage == true && projectCursor != null) {
                    if (hasNextPage == true && issuesCursor != null) {
                        viewModelScope.launch {
                            repository.getUserIssues(projectCursor, issuesCursor).collect {
                                _issues.value = it
                            }
                        }
                    }

                }
            }

        }

    }
}