package com.ahtat204.gitlab.presentation.viewmodels.project.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahtat204.gitlab.data.queries.GetRepositoryBlobsQuery
import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias File = GetRepositoryBlobsQuery.Node?

@HiltViewModel
class FileViewModel @Inject constructor(
    private val repository: GraphQlRepository
) : ViewModel() {
    private val _file = MutableStateFlow<File>(null)

    /**
     * Public immutable [StateFlow] of repository file.
     *
     * Emits the [File] retrieved via [loadRepositoryFile].
     */
    val file: StateFlow<File> = _file.asStateFlow()

    fun loadRepositoryFile(project: String, filePath: String, branch: String?) =
        viewModelScope.launch {
            val result = repository.getRepositoryBlobsContent(
                project = project,
                branch = branch,
                paths = listOf(filePath)
            )
            result.collect {
                val file = it.project?.repository?.blobs?.nodes?.firstOrNull()
                _file.value = file
            }
        }

}