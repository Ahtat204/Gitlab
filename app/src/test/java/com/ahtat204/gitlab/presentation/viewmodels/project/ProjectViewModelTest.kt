package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.data.remote.repositories.project.ProjectRepository
import com.ahtat204.gitlab.reponses.objects.mockProjectDetails
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectViewModelTest {

    private val repository: ProjectRepository = mock()
    private lateinit var viewModel: ProjectViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // Set the main dispatcher for viewModelScope
        Dispatchers.setMain(testDispatcher)
        viewModel = ProjectViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProject updates currentProject state correctly`() = runTest {
        // 1. Arrange: Prepare mock data from the Repository
        val projectId = "gid://gitlab/Project/123"


        // Mock the repository to return a Flow emitting our data
        whenever(repository.getProjectById(projectId)).thenReturn(flowOf(mockProjectDetails))

        // 2. Act: Trigger the ViewModel action
        viewModel.loadProject(projectId)

        // 3. Assert: Verify the StateFlow updated
        // Using Turbine makes this very clean

            assertEquals(viewModel.currentProject.value?.id,mockProjectDetails.project?.id )


        // Verify repository interaction
        verify(repository).getProjectById(projectId).first ()
    }
}
