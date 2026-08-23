package com.ahtat204.gitlab.data.remote.repositories.graphql

import com.ahtat204.gitlab.data.queries.cache.Cache.cache
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.json.mockedBranches
import com.ahtat204.gitlab.reponses.json.mockedCommits
import com.ahtat204.gitlab.reponses.json.mockedProject
import com.ahtat204.gitlab.reponses.json.mockedProjects
import com.ahtat204.gitlab.reponses.json.mockedRepository
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.memory.MemoryCacheFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Integration tests for [ApolloGraphQLRepository] using [MockWebServer].
 * 
 * These tests verify the integration between the repository, the Apollo GraphQL client, 
 * and the network layer by simulating real API responses.
 */
class ApolloGraphQLRepositoryTest {
    private lateinit var mockWebserver: MockWebServer
    private lateinit var apolloClient: ApolloClient
    private lateinit var repository: GraphQlRepository

    @Before
    fun setUp() {
        mockWebserver = MockWebServer()
        mockWebserver.start()
        apolloClient = ApolloClient.Builder()
            .serverUrl(mockWebserver.url("/graphql").toString())
            .cache(MemoryCacheFactory())
            .build()
        repository = ApolloGraphQLRepository(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebserver.shutdown()
    }

    @Test
    fun `getProjectById returns expected data when successful`() = runTest {
        // Arrange
        val projectId = "gid://gitlab/Project/123"
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedProject)
        )
        val result = repository.getProjectById(projectId).first()
        assertNotNull(result)
        assertNotNull(result?.project)
        assertEquals(projectId, result?.project?.id)
        assertEquals("gitlab", result?.project?.name)
        assertEquals("gitlab-org", result?.project?.namespace?.path)
        val recordedRequest = mockWebserver.takeRequest()
        assertEquals("/graphql", recordedRequest.path)
    }

    @Test
    fun `getAllProjects returns expected data when successful`() = runTest {
        // Arrange
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedProjects)
        )
        val result = repository.getAllProjects().first()
        assertNotNull(result)
        assertNotNull(result.currentUser?.namespace?.projects?.nodes)
        assertEquals(1, result.currentUser?.namespace?.projects?.nodes?.size)
        val firstProject = result.currentUser?.namespace?.projects?.nodes?.first()
        assertEquals("GitLab-Client", firstProject?.name)
        assertEquals("username/gitlab-client", firstProject?.fullPath)
        val recordedRequest = mockWebserver.takeRequest()
        assertEquals("/graphql", recordedRequest.path)
    }

    @Test
    fun `getProjectRepository returns expected data when successful`() = runTest {
        val projectId = "gid://gitlab/Project/12345"
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedRepository)
        )
        val result = repository.getProjectRepository(projectId, null).first()
        assertNotNull(result)
        assertNotNull(result?.project)
        assertNotNull(result?.project?.repository)
        assertNotNull(result?.project?.repository?.tree?.lastCommit)
        assertNotNull(result?.project?.repository?.tree?.lastCommit?.id)
        assertNotNull(result?.project?.repository?.tree?.lastCommit?.message)
        assertNotNull(result?.project?.repository?.tree?.lastCommit?.committedDate)
        assertNotNull(result?.project?.repository?.tree?.lastCommit?.author?.name)
        assertNotNull(result?.project?.repository?.rootRef)
        assertNotNull(result?.project?.repository?.tree)
        assertNotNull(result?.project?.repository?.tree?.trees)
        assertNotNull(result?.project?.repository?.tree?.blobs)
        assertEquals(result?.project?.id, projectId)
        assertEquals(result?.project?.name, "awesome-android-app")
        assertEquals(result?.project?.repository?.rootRef, "main")
        assertEquals(result?.project?.repository?.tree?.blobs?.nodes?.first()?.name, "README.md")
        assertEquals(
            result?.project?.repository?.tree?.blobs?.nodes?.first()?.id,
            "gid://gitlab/Blob/456"
        )
        assertEquals(
            result?.project?.repository?.tree?.trees?.nodes?.first()?.id,
            "gid://gitlab/Tree/789"
        )
        assertEquals(result?.project?.repository?.tree?.trees?.nodes?.first()?.name, "src")
        assertEquals(
            result?.project?.repository?.tree?.lastCommit?.id,
            "gid://gitlab/Commit/abc123def456"
        )
        val recordedRequest = mockWebserver.takeRequest()
        assertEquals("/graphql", recordedRequest.path)
    }

    @Test
    fun `getRepositoryBranches returns expected data when successful`() = runTest {
        val projectId = "gid://gitlab/Project/1"
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedBranches)
        )
        val result = repository.getRepositoryBranches(project = projectId, skip = 0).first()
        assertNotNull(result.project)
        assertNotNullAndEquals(result.project?.id, projectId)
        assertNotNullAndEquals(result.project?.repository?.branchNames?.first(), "main")
    }

    @Test
    fun `getRepositoryCommits returns expected data when successful`() = runTest {
        val projectId = "gid://gitlab/Project/1"
        mockWebserver.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockedCommits)
        )

        val result = repository.getProjectCommits(projectId, branch = "main", null).first()
        assertNotNullAndEquals(result?.project?.id, projectId)
        assertNotNullAndEquals(result?.project?.repository?.branchNames?.first(), "main")
        assertNotNullAndEquals(
            result?.project?.repository?.commits?.nodes?.first()?.id,
            "gid://gitlab/Commit/a1b2c3d4"
        )
        assertNotNullAndEquals(
            result?.project?.repository?.commits?.nodes?.first()?.committedDate,
            "2023-10-27T14:30:00Z"
        )
        assertNotNullAndEquals(
            result?.project?.repository?.commits?.nodes?.first()?.name,
            "feat: implement repository history view"
        )

    }
}
