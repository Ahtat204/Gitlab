package com.ahtat204.gitlab.presentation.viewmodels

import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.objects.mockedProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val repository: GraphQlRepository = mock()
    private lateinit var viewModel: ProfileViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProfileViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadProfile() = runTest(testDispatcher) {
        whenever(repository.getMyProfile()).thenReturn(flowOf(mockedProfile))
        viewModel.loadProfile()
        val profile = viewModel.currentUser.value
        val expected = mockedProfile.currentUser
        Assert.assertNotNull(profile)
        assertNotNullAndEquals(profile?.name, expected?.name!!)
        assertNotNullAndEquals(profile?.id, expected.id)
        assertNotNullAndEquals(profile?.avatarUrl, expected.avatarUrl!!)
        assertNotNullAndEquals(profile?.status, expected.status!!)
        assertNotNullAndEquals(profile?.username, expected.username)
        assertNotNullAndEquals(profile?.bio, expected.bio!!)
        assertNotNullAndEquals(profile?.github, expected.github!!)
        assertNotNullAndEquals(profile?.linkedin, expected.linkedin!!)
        assertNotNullAndEquals(profile?.location, expected.location!!)
        assertNotNullAndEquals(profile?.projectCount, expected.projectCount!!)
        assertNotNullAndEquals(profile?.webUrl, expected.webUrl)

    }

}