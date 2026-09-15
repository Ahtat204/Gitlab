package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockProjectPipelinesData
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
class PipelinesViewModelTest : TestBase() {
    private lateinit var viewModel: PipelinesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PipelinesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProjectPipelines() = runTest(testDispatcher) {
        val projectId = "gid://gitlab/Project/123"
        whenever(repository.getProjectPipelines(projectId)).thenReturn(
            flowOf(
                mockProjectPipelinesData
            )
        )
        viewModel.loadProjectPipelines(projectId)
        val pipelines = viewModel.pipelines.value
        assertNotNull(pipelines)
        val nodes = pipelines!!.nodes
        assertNotNull(nodes)
        assertFalse(nodes!!.isEmpty())
        val mockedPage = mockProjectPipelinesData.project!!.pipelines!!.pageInfo
        val mockedNodes = mockProjectPipelinesData.project!!.pipelines!!.nodes
        for (i in 0 until nodes.size) {
            assertNotNullAndEquals(
                nodes[i]!!.id,
                mockedNodes!![i]!!.id
            )
            assertNotNullAndEquals(nodes[i]!!.type, mockedNodes[i]!!.type)
            assertNotNullAndEquals(nodes[i]!!.status, mockedNodes[i]!!.status)
            assertNotNull(nodes[i]!!.commit)
            assertNotNullAndEquals(nodes[i]!!.commit!!.name, mockedNodes[i]!!.commit!!.name!!)
            assertNotNull(nodes[i]!!.mergeRequest)
            assertNotNullAndEquals(
                nodes[i]!!.mergeRequest!!.name,
                mockedNodes[i]!!.mergeRequest!!.name!!
            )
            assertNotNullAndEquals(nodes[i]!!.duration, mockedNodes[i]!!.duration!!)
            assertNotNull(nodes[i]!!.user)
            assertNotNullAndEquals(nodes[i]!!.user!!.name, mockedNodes[i]!!.user!!.name)
            assertNotNullAndEquals(nodes[i]!!.finishedAt, mockedNodes[i]!!.finishedAt!!)
            assertNotNullAndEquals(nodes[i]!!.ref, mockedNodes[i]!!.ref!!)
        }
        val page = pipelines!!.pageInfo
        assertNotNullAndEquals(page.startCursor, mockedPage.startCursor!!)
        assertNotNullAndEquals(page.hasNextPage, mockedPage!!.hasNextPage)
        assertNotNullAndEquals(page.endCursor, mockedPage.endCursor!!)
        assertNotNullAndEquals(page.hasPreviousPage, mockedPage!!.hasPreviousPage)
    }

}