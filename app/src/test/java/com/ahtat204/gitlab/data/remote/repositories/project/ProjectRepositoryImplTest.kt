package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.ApolloResponse.Builder
import com.apollographql.apollo.testing.QueueTestNetworkTransport
import com.apollographql.mockserver.MockResponse
import com.apollographql.mockserver.MockServer
import com.apollographql.mockserver.enqueueString
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ProjectRepositoryImplTest {
    private lateinit var mockServer: MockServer
    private lateinit var apolloClient: ApolloClient
    private lateinit var repository: ProjectRepository

    @OptIn(ApolloExperimental::class)
    private val networkTransport = QueueTestNetworkTransport()

    @OptIn(ApolloExperimental::class)
    @Before
    fun setUp() = runBlocking {
        mockServer = MockServer()
        apolloClient = ApolloClient.Builder().networkTransport(
            networkTransport
        ).build()
        repository = ProjectRepositoryImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockServer.close()
    }

    @Test
    fun getAllProjects() {
    }

    @OptIn(ApolloExperimental::class)
    @Test
    fun getProjectById() = runBlocking {
        val mockResponse = GetProjectDetailsQuery.Data(
            project = GetProjectDetailsQuery.Project(
                __typename = "Project",
                pipelineCounts = GetProjectDetailsQuery.PipelineCounts(
                    __typename = "PipelineCounts", pending = 2, running = 1
                ),
                namespace = GetProjectDetailsQuery.Namespace(
                    __typename = "Namespace", path = "gitlab-org"
                ),
                openIssuesCount = 42,
                fullPath = "gitlab-org/gitlab",
                openMergeRequestsCount = 15,
                forksCount = 5000,
                starCount = 12000,
                id = "gid://gitlab/Project/123",
                name = "gitlab",
                description = "The GitLab community edition codebase."
            )
        )
        val project = GetProjectDetailsQuery.Data {

        }
        val json =
            "  {\n" + "  \"project\": {\n" + "    \"__typename\": \"Project\",\n" + "    \"pipelineCounts\": {\n" + "      \"__typename\": \"PipelineCounts\",\n" + "      \"pending\": 2,\n" + "      \"running\": 1\n" + "    },\n" + "    \"namespace\": {\n" + "      \"__typename\": \"Namespace\",\n" + "      \"path\": \"gitlab-org\"\n" + "    },\n" + "    \"openIssuesCount\": 42,\n" + "    \"fullPath\": \"gitlab-org/gitlab\",\n" + "    \"openMergeRequestsCount\": 15,\n" + "    \"forksCount\": 5000,\n" + "    \"starCount\": 12000,\n" + "    \"id\": \"gid://gitlab/Project/123\",\n" + "    \"name\": \"gitlab\",\n" + "    \"description\": \"The GitLab community edition codebase.\"\n" + "  }\n" + "}  "
        val apolloResponse = Builder(
            operation = operation()
            , requestUuid = UUID.randomUUID()
        ).data(mockResponse).isLast(true).build()
        /*networkTransport.enqueue(
            response = ApolloResponse()
        )*/
        mockServer.enqueueString(json)
        mockServer.enqueue(
            MockResponse.Builder().body(json).build()
        )
        val response = apolloClient.query(GetProjectDetailsQuery("0")).execute()
        assertNotNull(response)
        val errors = response.errors
        assertNull(errors)
        assertNotNull(response.data)
    }

    @Test
    fun getProjectCommits() {
    }

    @Test
    fun getRepositorySubTree() {
    }

    @Test
    fun getRepositoryBranches() {
    }

    @Test
    fun getProjectRepository() {
    }

}