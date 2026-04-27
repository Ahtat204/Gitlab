package com.asue24.gitlab.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asue24.gitlab.GetMyProjectsQuery.Node
import com.asue24.gitlab.data.repositories.project.ProjectRepository
import com.asue24.gitlab.data.repositories.project.ProjectRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class ProjectViewModel() : ViewModel() {
    //TODO:this will be refactored to Dependency Injection,we're just testing now
    private val projectRepository: ProjectRepository=ProjectRepositoryImpl()
    /**
     * Extremely fast project lookup to avoid frequent API requests .
     */
    private val cache: ConcurrentHashMap<String, Node> = ConcurrentHashMap()
    private val _projects = MutableStateFlow<List<Node>>(emptyList())
    val projects: StateFlow<List<Node>> = _projects.asStateFlow()
    public fun GetAllMyProjects(){
     viewModelScope.launch (Dispatchers.IO) {
            projectRepository.getAllProjects().collect { data ->
                val newNodes = data.currentUser?.contributedProjects?.nodes?.filterNotNull() ?: emptyList()
                newNodes.forEach { node ->
                    cache[node.id] = node
                }

                // Update the UI stream with the latest from the cache
                // Converting map values to a list is O(N), which is fine for a
                // fixed-size project list.
                _projects.value = cache.values.toList()
            }
        }

    }
}
