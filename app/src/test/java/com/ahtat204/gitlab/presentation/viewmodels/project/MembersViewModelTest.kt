package com.ahtat204.gitlab.presentation.viewmodels.project

import com.ahtat204.gitlab.presentation.viewmodels.TestBase
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedMembers
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
class MembersViewModelTest : TestBase() {
    private lateinit var viewModel: MembersViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MembersViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProjectMembers() = runTest {
        val projectId = "gid://gitlab/Project/101"
        whenever(repository.getProjectMembers(projectId)).thenReturn(
            flowOf(
                mockedMembers
            )
        )
        viewModel.loadProjectMembers(projectId)
        val value = viewModel.members.value
        assertNotNull(value)
        value!!
        assertNotNullAndEquals(value.id, projectId)
        val members = value.projectMembers
        assertNotNull(members)
        members!!
        val nodes = members.nodes
        assertNotNull(nodes)
        nodes!!
        assertFalse(nodes.isEmpty())
        val mockedNodes = mockedMembers.project?.projectMembers?.nodes!!

        for (i in 0 until nodes.size) {
            val actual = nodes[i]
            val expect = mockedNodes[i]!!
            assertNotNullAndEquals(
                actual?.id,
                expect.id
            )
            assertNotNullAndEquals(actual?.createdAt, expect.createdAt!!)
            val user = actual?.user
            val person = expect.user!!
            assertNotNullAndEquals(user?.name, person.name)
            assertNotNullAndEquals(user?.username, person.username)
            assertNotNullAndEquals(user?.bio, person.bio!!)
        }
        val page = members.pageInfo
        val mockPage = mockedMembers.project.projectMembers.pageInfo
        assertNotNullAndEquals(page.hasNextPage, mockPage.hasNextPage)
        assertNotNullAndEquals(page.endCursor, mockPage.endCursor!!)
        assertNotNullAndEquals(page.hasPreviousPage, mockPage.hasPreviousPage)
        assertNotNullAndEquals(page.startCursor, mockPage.startCursor!!)
    }

}