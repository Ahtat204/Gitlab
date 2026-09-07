package com.ahtat204.gitlab.presentation.viewmodels.project.build

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedPipeline
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
class PipelineViewModelTest : TestBase() {
    private lateinit var viewModel: PipelineViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PipelineViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProjectPipeline() = runTest {
        val projectId = "gid://gitlab/Project/1"
        val pipelineId = "gid://gitlab/Ci::Pipeline/8812345"
        whenever(repository.getProjectPipeline(projectId, pipelineId)).thenReturn(
            flowOf(
                mockedPipeline
            )
        )
        viewModel.loadProjectPipeline(projectId, pipelineId)
        val pipeline = viewModel.pipeline.value
        val mockedPipe = mockedPipeline.project!!.pipeline
        assertNotNull(pipeline)
        assertNotNullAndEquals(pipeline!!.name, mockedPipe!!.name!!)
        assertNotNullAndEquals(pipeline.id, mockedPipe.id)
        assertNotNullAndEquals(pipeline.type, mockedPipe.type)
        assertNotNullAndEquals(pipeline.name, mockedPipe.name)
        assertNotNullAndEquals(pipeline.computeMinutes, mockedPipe.computeMinutes!!)
        val jobs = pipeline.jobs
        assertNotNull(jobs)
        val nodes = jobs!!.nodes
        assertNotNull(nodes)
        assertFalse(nodes!!.isEmpty())
        val mockedJobs = mockedPipe.jobs
        val mockedNodes = mockedJobs!!.nodes
        for (i in 0 until nodes.size) {
            assertNotNullAndEquals(nodes[i]!!.id, mockedNodes!![i]!!.id!!)
            assertNotNullAndEquals(nodes[i]?.status, mockedNodes[i]?.status!!)
            assertNotNullAndEquals(nodes[i]!!.duration, mockedNodes[i]!!.duration!!)
            assertNotNullAndEquals(nodes[i]!!.createdAt, mockedNodes[i]!!.createdAt)
        }
        val page = jobs.pageInfo
        val mockedPage = mockedJobs.pageInfo
        assertNotNullAndEquals(page.hasNextPage, mockedPage.hasNextPage)
        assertNotNullAndEquals(page.endCursor!!, mockedPage.endCursor!!)
        assertNotNullAndEquals(page.startCursor!!, mockedPage.startCursor!!)


    }

}