package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

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
    fun loadAllProjects() {
    }

    @Test
    fun `loadProject updates currentProject state correctly`() = runTest(testDispatcher) {

    }

}