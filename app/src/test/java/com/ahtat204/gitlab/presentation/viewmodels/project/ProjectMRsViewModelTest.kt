package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedMRs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProjectMRsViewModelTest : TestBase() {
    private lateinit var viewModel: ProjectMRsViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProjectMRsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProjectMRs() = runTest {
        val projectId = "gid://gitlab/Project/123"
        whenever(repository.getProjectMergeRequests(projectId)).thenReturn(
            flowOf(
                mockedMRs
            )
        )
        viewModel.loadProjectMRs(projectId)
        val mrs = viewModel.mrs.value
        assertNotNull(mrs)
        val nodes = mrs?.nodes
        assertNotNull(nodes)
        assertFalse(nodes?.isEmpty() == true)
        val mockedNodes = mockedMRs.project?.mergeRequests?.nodes
        for (i in 0 until nodes!!.size) {
            assertNotNullAndEquals(
                nodes[i]!!.id,
                mockedNodes!![i]!!.id
            )
            assertNotNullAndEquals(nodes[i]!!.name, mockedNodes[i]!!.name!!)
            assertNotNullAndEquals(nodes[i]!!.state, mockedNodes[i]!!.state)
            assertNotNull(nodes[i]!!.author)
            assertNotNullAndEquals(nodes[i]!!.author!!.name, mockedNodes[i]!!.author!!.name)
            assertNotNull(nodes[i]!!.pipelines)
            assertNotNullAndEquals(
                nodes[i]!!.pipelines?.edges?.get(0),
                mockedNodes[i]!!.pipelines?.edges?.get(0)!!
            )
            assertNotNullAndEquals(nodes[i]!!.conflicts, mockedNodes[i]!!.conflicts)
            assertNotNull(nodes[i]!!.labels)
            val labels = nodes[i]?.labels?.nodes
            val mockedLabels = mockedNodes[i]?.labels?.nodes

            assertNotNullAndEquals(labels?.getOrNull(0)?.title, mockedLabels?.getOrNull(0)?.title!!)
            assertNotNullAndEquals(labels?.getOrNull(0)?.color, mockedLabels.getOrNull(0)?.color!!)
            assertNotNullAndEquals(labels?.getOrNull(0)?.id, mockedLabels.getOrNull(0)?.id!!)
            assertFalse(labels?.isEmpty() == true)
            assertNotNullAndEquals(nodes[i]!!.sourceBranch, mockedNodes[i]!!.sourceBranch)
            assertNotNullAndEquals(nodes[i]!!.targetBranch, mockedNodes[i]!!.targetBranch)
            assertNotNullAndEquals(nodes[i]!!.updatedAt, mockedNodes[i]!!.updatedAt)
            assertNotNullAndEquals(nodes[i]!!.createdAt, mockedNodes[i]!!.createdAt)
            assertNotNullAndEquals(nodes[i]!!.iid, mockedNodes[i]!!.iid)

        }


    }

}