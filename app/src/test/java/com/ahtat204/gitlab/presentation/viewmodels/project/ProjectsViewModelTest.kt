package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedAllProjects
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
class ProjectsViewModelTest : TestBase() {
    private lateinit var viewModel: ProjectsViewModel


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProjectsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadCurrentUserProjects() = runTest {
        whenever(repository.getAllProjects()).thenReturn(flowOf(mockedAllProjects))
        viewModel.loadAllProjects()
        val value = viewModel.projects.value
        assertNotNull(value)
        val page = value!!.pageInfo
        assertNotNullAndEquals(
            page.hasNextPage,
            mockedAllProjects.currentUser!!.projectMemberships!!.pageInfo.hasNextPage
        )
        assertNotNullAndEquals(
            page.hasPreviousPage,
            mockedAllProjects.currentUser.projectMemberships.pageInfo.hasPreviousPage
        )
        assertNotNullAndEquals(
            page.endCursor, mockedAllProjects.currentUser.projectMemberships.pageInfo.endCursor!!
        )
        val nodes = value.nodes
        assertNotNull(nodes)
        assertFalse(nodes!!.isEmpty())
        // val first=projects[0]
        for (j in 0 until nodes.size) {
            val node = nodes[j]!!.project
            val mockedNode = mockedAllProjects.currentUser!!.projectMemberships!!.nodes!![j]!!
            assertNotNullAndEquals(
                nodes[j]?.id, mockedNode.id
            )
            assertNotNullAndEquals(
                node?.name, mockedNode.project!!.name
            )
            assertNotNullAndEquals(
                node?.fullPath, mockedNode.project.fullPath
            )
            assertNotNullAndEquals(
                node?.description, mockedNode.project.description!!
            )
            assertNotNullAndEquals(
                node?.visibility, mockedNode.project.visibility!!
            )
            assertNotNullAndEquals(
                node?.description, mockedNode.project.description!!
            )
            val size = node?.topics?.size!!
            assertNotNullAndEquals(
                size, mockedNode.project.topics!!.size
            )
            for (i in 0 until size) {
                assertNotNullAndEquals(
                    node.topics[i], mockedNode.project.topics[i]
                )
            }
            val languages = node.languages?.size!!
            assertNotNullAndEquals(
                languages, mockedNode.project.languages!!.size
            )
            for (i in 0 until languages) {
                assertNotNullAndEquals(
                    node.languages[i], mockedNode.project.languages[i]
                )
            }
        }


    }

}