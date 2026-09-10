package com.ahtat204.gitlab.presentation.viewmodels.project.build

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedPipelineJob
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class JobViewModelTest : TestBase() {
    private lateinit var viewModel: JobViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = JobViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadPipelineJob() = runTest {
        val projectId = "gid://gitlab/Project/12345"
        val jobId = "gid://gitlab/Ci::Build/987654"
        whenever(repository.getPipelineJob(projectId, jobId)).thenReturn(
            flowOf(
                mockedPipelineJob
            )
        )
        viewModel.loadPipelineJob(projectId, jobId)
        val job = viewModel.job.value
        assertNotNull(job)
        assertNotNullAndEquals(job?.name, "build-android-debug")
        assertNotNullAndEquals(job?.createdAt, "2023-10-27T10:00:00Z")
        assertNotNullAndEquals(job?.duration, 345)
        assertNotNullAndEquals(job?.id, "gid://gitlab/Ci::Build/987654")
    }

}