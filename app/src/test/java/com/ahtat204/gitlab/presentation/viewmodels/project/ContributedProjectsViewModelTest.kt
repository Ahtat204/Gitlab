package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.data.queries.GetMyContributedProjectsQuery.Language
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockContributedProject
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

class ContributedProjectsViewModelTest : TestBase() {
    private lateinit var viewModel: ContributedProjectsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ContributedProjectsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun loadAllProjects() = runTest(testDispatcher) {
        whenever(repository.getAllMyContributedProjects()).thenReturn(flowOf(mockContributedProject))
        viewModel.loadAllProjects()
        assertNotNull(viewModel.projects.value)
        val projects = viewModel.projects.value
        val contributedProjects = projects?.contributedProjects
        assertNotNull(contributedProjects)
        contributedProjects!!
        val nodes = contributedProjects.nodes
        assertNotNull(nodes)
        nodes!!
        val size = nodes.size
        val mockNodes = mockContributedProject.currentUser?.contributedProjects?.nodes!!
        Assert.assertFalse(size == 0)
        for (i in 0 until size) {
            val node = nodes[i]
            val moNode = mockNodes[i]
            assertNotNullAndEquals(node?.name, moNode!!.name)
            assertNotNullAndEquals(node?.id, moNode.id)
            assertNotNullAndEquals(node?.fullPath, moNode.fullPath)
            assertNotNullAndEquals(node?.description, moNode.description!!)
            assertNotNullAndEquals(node?.visibility, moNode.visibility!!)
            assertNotNullAndEquals(node?.lastActivityAt, moNode.lastActivityAt!!)
            val languages = node?.languages
            val moLanguages = moNode.languages
            assertNotNullAndEquals(languages?.getOrNull(0), moLanguages?.getOrNull(0) as Language)
            val pipelines = node?.pipelines?.nodes
            val moPipelines = moNode.pipelines?.nodes
            assertNotNullAndEquals(
                pipelines?.getOrNull(0)?.id,
                moPipelines?.getOrNull(0)?.id as String
            )
            assertNotNullAndEquals(
                pipelines?.getOrNull(0)?.status,
                moPipelines.getOrNull(0)?.status as PipelineStatusEnum
            )
            val topics = node?.topics
            val moTopics = moNode.topics
            assertNotNullAndEquals(topics?.getOrNull(0), moTopics?.getOrNull(0) as String)

        }
    }

    @Test
    fun `loadProject by Id`() = runTest(testDispatcher) {
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

}