package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedIssues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class IssuesViewModelTest : TestBase() {
    private lateinit var viewModel: IssuesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = IssuesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProjectIssues() = runTest {
        val projectId = "gid://gitlab/Project/101"
        whenever(repository.getProjectIssues(projectId)).thenReturn(
            flowOf(
                mockedIssues
            )
        )
        viewModel.loadProjectIssues(projectId)
        val value = viewModel.issues.value
        assertNotNull(value)
        value!!
        assertNotNullAndEquals(value.id, projectId)
        val members = value.issues
        assertNotNull(members)
        members!!
        val nodes = members.nodes
        assertNotNull(nodes)
        nodes!!
        assertFalse(nodes.isEmpty())
        val mockedNodes = mockedIssues.project?.issues?.nodes!!

        for (i in 0 until nodes.size) {
            val actual = nodes[i]
            val expect = mockedNodes[i]!!
            assertNotNullAndEquals(
                actual?.id,
                expect.id
            )
            assertNotNullAndEquals(actual?.createdAt, expect.createdAt)
            assertNotNullAndEquals(actual?.name, expect.name!!)
            assertNotNullAndEquals(actual?.title, expect.title)
            assertNotNullAndEquals(actual?.state, expect.state)
            val firstAssignee = actual?.assignees?.nodes!![0]?.name
            val expectedAssignee = expect.assignees?.nodes!![0]?.name
            Assert.assertEquals(firstAssignee, expectedAssignee)

        }
        val page = members.pageInfo
        val mockPage = mockedIssues.project?.issues?.pageInfo!!
        assertNotNullAndEquals(page.hasNextPage, mockPage.hasNextPage)
        assertNotNullAndEquals(page.endCursor, mockPage.endCursor!!)
        assertNotNullAndEquals(page.startCursor, mockPage.startCursor!!)
    }

}