package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockDataList
import com.ahtat204.gitlab.reponses.objects.mockProjectDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectViewModelTest : TestBase() {
    private lateinit var viewModel: ProjectViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProjectViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProject updates currentProject state correctly`() = runTest(testDispatcher) {
        val projectId = "gid://gitlab/Project/123"
        whenever(repository.getProjectById(projectId)).thenReturn(flowOf(mockProjectDetails))
        viewModel.loadProject(projectId)
        val project = viewModel.currentProject.value
        assertEquals(project?.id, mockProjectDetails.project!!.id)
        assertNotNullAndEquals(project?.name, mockProjectDetails.project.name)
        assertNotNullAndEquals(project?.fullPath, mockProjectDetails.project.fullPath)
        assertNotNullAndEquals(project?.forksCount, mockProjectDetails.project.forksCount)
        assertNotNullAndEquals(
            project?.openIssuesCount,
            mockProjectDetails.project.openIssuesCount!!
        )
        assertNotNullAndEquals(project?.pipelineCounts, mockProjectDetails.project.pipelineCounts!!)
        assertNotNullAndEquals(project?.starCount, mockProjectDetails.project.starCount)
    }

    @Test
    fun `load a List of projects updates _projects state `() = runTest(testDispatcher) {
        whenever(repository.getAllPersonalProjects()).thenReturn(flowOf(mockDataList))
        viewModel.loadAllProjects()
        assertNotNull(viewModel.projects.value)
        val projects = viewModel.projects.value
        assertNotNull(projects?.namespace)
        assertNotNullAndEquals(projects?.id, "user-101")
        assertNotNullAndEquals(projects?.avatarUrl, "https://example.com")
        assertNotNull(projects?.namespace?.projects)
        assertNotNullAndEquals(projects?.namespace?.projects?.pageInfo?.hasNextPage, true)
        assertNotNullAndEquals(
            projects?.namespace?.projects?.pageInfo?.startCursor,
            "eyJjdXJzb3IiOiIxIn0="
        )
        var nodes = projects?.namespace?.projects?.nodes
        assertNotNull(nodes)
        nodes = nodes!!
        Assert.assertFalse(nodes.isEmpty())
        for (j in 0 until nodes.size) {
            val node = nodes[j]
            assertNotNullAndEquals(
                node?.id,
                mockDataList.currentUser!!.namespace!!.projects.nodes!![j]!!.id
            )
            assertNotNullAndEquals(
                node?.name,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.name
            )
            assertNotNullAndEquals(
                node?.fullPath,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.fullPath
            )
            assertNotNullAndEquals(
                node?.description,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.description!!
            )
            assertNotNullAndEquals(
                node?.visibility,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.visibility!!
            )
            assertNotNullAndEquals(
                node?.lastActivityAt,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.lastActivityAt!!
            )
            assertNotNullAndEquals(
                node?.description,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.description!!
            )
            val size = node?.topics?.size!!
            assertNotNullAndEquals(
                size,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.topics!!.size
            )
            for (i in 0 until size) {
                assertNotNullAndEquals(
                    node.topics[i],
                    mockDataList.currentUser.namespace.projects.nodes[j]!!.topics!![i]
                )
            }
            val languages = node.languages?.size!!
            assertNotNullAndEquals(
                languages,
                mockDataList.currentUser.namespace.projects.nodes[j]!!.languages!!.size
            )
            for (i in 0 until languages) {
                assertNotNullAndEquals(
                    node.languages[i],
                    mockDataList.currentUser.namespace.projects.nodes[j]!!.languages!![i]
                )
            }
        }
    }
}
