package com.ahtat204.gitlab.data.repositories

import com.ahtat204.gitlab.data.repositories.project.ProjectRepository
import com.ahtat204.gitlab.data.repositories.project.ProjectRepositoryImpl
import com.apollographql.apollo.ApolloClient
import com.apollographql.mockserver.MockServer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ProjectRepositoryImplTest {
    val mockServer = MockServer()
    lateinit var apolloClient: ApolloClient
    lateinit var projectRepository: ProjectRepository

    @Before
    fun setUp() = runBlocking {
        apolloClient = ApolloClient.Builder().serverUrl(mockServer.url()).build()
        projectRepository = ProjectRepositoryImpl(apolloClient)
    }

    @Test
    fun getAllProjects() {
        assertNotNull(apolloClient)
        assertNotNull(projectRepository)
    }

    @Test
    fun getProjectById() {
    }

}