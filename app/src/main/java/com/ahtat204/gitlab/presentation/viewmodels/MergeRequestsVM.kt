package com.ahtat204.gitlab.presentation.viewmodels

import com.ahtat204.gitlab.data.queries.GetMyMergeRequestsQuery
import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Type alias for the Merge Requests data structure from the GraphQL query.
 */
typealias MergeRequests = GetMyMergeRequestsQuery.Data?

/**
 * ViewModel responsible for managing the state and logic for the Merge Requests screen.
 *
 * @param projectRepository The repository used to fetch merge request data.
 * @author Lahcen AHTAT
 */
class MergeRequestsVM @Inject internal constructor(private val projectRepository: ProjectRepository) {
    /** Internal mutable state for merge requests. */
    private val _mrs = MutableStateFlow<MergeRequests>(null)

    /** Public immutable stream of merge requests. */
    val mrs: StateFlow<MergeRequests> = _mrs.asStateFlow()

    /**
     * Triggers the loading of merge requests for the current user.
     *
     * Note: Current implementation is a placeholder and needs to be completed.
     */
    fun loadMyMergeRequests() {

    }
}
