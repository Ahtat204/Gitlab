package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.Responses
import com.ahtat204.gitlab.data.queries.cache.Cache.cache
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Integration tests for [ProjectRepositoryImpl] using [MockWebServer].
 * 
 * These tests verify the integration between the repository, the Apollo GraphQL client, 
 * and the network layer by simulating real API responses.
 */
class ProjectRepositoryImplTest : Responses() {
    private lateinit var mockWebserver: MockWebServer
    private lateinit var apolloClient: ApolloClient
    private lateinit var repository: ProjectRepository

    @Before
    fun setUp() {
        mockWebserver = MockWebServer()
        mockWebserver.start()
        
        // Build ApolloClient pointing to the mock server with an in-memory normalized cache
        // This is necessary because the repository uses .watch()
        apolloClient = ApolloClient.Builder()
            .serverUrl(mockWebserver.url("/graphql").toString())
            .cache(MemoryCacheFactory())
            .build()
            
        repository = ProjectRepositoryImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebserver.shutdown()
    }

    @Test
    fun `getProjectById returns expected data when successful`() = runBlocking {
        // Arrange
        val projectId = "gid://gitlab/Project/123"
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedProject)
        )

        // Act
        // We use .first() because getProjectById returns a Flow that watches for changes.
        // The first emission will be the one triggered by the network request.
        val result = repository.getProjectById(projectId).first()

        // Assert
        assertNotNull(result)
        assertNotNull(result?.project)
        assertEquals(projectId, result?.project?.id)
        assertEquals("gitlab", result?.project?.name)
        assertEquals("gitlab-org", result?.project?.namespace?.path)
        
        // Verify the request made to the mock server
        val recordedRequest = mockWebserver.takeRequest()
        assertEquals("/graphql", recordedRequest.path)
    }

    @Test
    fun `getAllProjects returns expected data when successful`() = runBlocking {
        // Arrange
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedProjects)
        )

        // Act
        val result = repository.getAllProjects().first()

        // Assert
        assertNotNull(result)
        assertNotNull(result.currentUser?.namespace?.projects?.nodes)
        assertEquals(1, result.currentUser?.namespace?.projects?.nodes?.size)
        val firstProject = result.currentUser?.namespace?.projects?.nodes?.first()
        assertEquals("GitLab-Client", firstProject?.name)
        assertEquals("username/gitlab-client", firstProject?.fullPath)
        
        // Verify request
        val recordedRequest = mockWebserver.takeRequest()
        assertEquals("/graphql", recordedRequest.path)
    }
}
