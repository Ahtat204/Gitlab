package com.ahtat204.gitlab.data.remote.repositories.graphql

import com.ahtat204.gitlab.data.queries.cache.Cache.cache
import com.ahtat204.gitlab.data.queries.type.MergeRequestState
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.reponses.json.assertNotNullAndEquals
import com.ahtat204.gitlab.reponses.json.mockedAllProjects
import com.ahtat204.gitlab.reponses.json.mockedBranches
import com.ahtat204.gitlab.reponses.json.mockedCommits
import com.ahtat204.gitlab.reponses.json.mockedMembers
import com.ahtat204.gitlab.reponses.json.mockedMrs
import com.ahtat204.gitlab.reponses.json.mockedPipelines
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Integration tests for [ApolloGraphQLRepository] using [MockWebServer].
 * 
 * These tests verify the integration between the repository, the Apollo GraphQL client, 
 * and the network layer by simulating real API responses.
 */
@Suppress("DEPRECATION")
class ApolloGraphQLRepositoryTest {
    private lateinit var mockWebserver: MockWebServer
    private lateinit var apolloClient: ApolloClient
    private lateinit var repository: GraphQlRepository

    @Before
    fun setUp() {
        mockWebserver = MockWebServer()
        mockWebserver.start()
        apolloClient = ApolloClient.Builder().serverUrl(mockWebserver.url("/graphql").toString())
            .cache(MemoryCacheFactory()).build()
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
            MockResponse().setResponseCode(200).setBody(mockedProject)
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
            MockResponse().setResponseCode(200).setBody(mockedProjects)
        )
        val result = repository.getAllPersonalProjects().first()
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
            MockResponse().setResponseCode(200).setBody(mockedRepository)
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
            result?.project?.repository?.tree?.blobs?.nodes?.first()?.id, "gid://gitlab/Blob/456"
        )
        assertEquals(
            result?.project?.repository?.tree?.trees?.nodes?.first()?.id, "gid://gitlab/Tree/789"
        )
        assertEquals(result?.project?.repository?.tree?.trees?.nodes?.first()?.name, "src")
        assertEquals(
            result?.project?.repository?.tree?.lastCommit?.id, "gid://gitlab/Commit/abc123def456"
        )
        val recordedRequest = mockWebserver.takeRequest()
        assertEquals("/graphql", recordedRequest.path)
    }

    @Test
    fun `getRepositoryBranches returns expected data when successful`() = runTest {
        val projectId = "gid://gitlab/Project/1"
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedBranches)
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
            MockResponse().setResponseCode(200).setBody(mockedCommits)
        )

        val result = repository.getProjectCommits(projectId, branch = "main", null).first()
        assertNotNullAndEquals(result?.project?.id, projectId)
        assertNotNullAndEquals(result?.project?.repository?.branchNames?.first(), "main")
        assertNotNullAndEquals(
            result?.project?.repository?.commits?.nodes?.first()?.id, "gid://gitlab/Commit/a1b2c3d4"
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

    @Test
    fun `get project pipelines returns a list of non-null pipelines`() = runTest {
        val projectId = "project-id-123"
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedPipelines)
        )
        val result = repository.getProjectPipelines(projectId).first()
        val project = result.project
        assertNotNull(project)
        val pipelines = project?.pipelines
        assertNotNull(pipelines)
        val nodes = pipelines?.nodes
        assertNotNull(nodes)
        assertFalse(nodes!!.isEmpty())
        val first = nodes[0]!!
        assertNotNullAndEquals(first.id, "pipeline-id-001")
        assertNotNullAndEquals(first.type, "CI_BRANCH")
        assertNotNullAndEquals(first.status, PipelineStatusEnum.SUCCESS)
        assertNotNull(first.commit)
        assertNotNullAndEquals(first.commit!!.name, "feat: implement user authentication")
        assertNotNull(first.mergeRequest)
        assertNotNullAndEquals(first.mergeRequest!!.name, "implementing Kafka Consumer")
        assertNotNullAndEquals(first.duration, 320)
        assertNotNull(first.user)
        assertNotNullAndEquals(first.user!!.name, "Alex Developer")
        assertNotNullAndEquals(first.finishedAt, "2026-08-25T20:00:00Z")
        assertNotNullAndEquals(first.ref, "main")
        val second = nodes[1]
        assertNotNullAndEquals(second!!.id, "pipeline-id-002")
        assertNotNullAndEquals(second.type, "CI_MERGE_REQUEST")
        assertNotNullAndEquals(second.status, PipelineStatusEnum.FAILED)
        assertNotNull(second.commit)
        assertNotNullAndEquals(second.commit!!.name, "fix: update payload constraints")
        assertNotNull(second.mergeRequest)
        assertNotNullAndEquals(second.mergeRequest!!.name, "Resolve API payload serialization bug")
        assertNotNullAndEquals(second.duration, 45)
        assertNotNull(second.user)
        assertNotNullAndEquals(second.user!!.name, "****")
        assertNotNullAndEquals(second.finishedAt, "2026-08-25T21:15:00Z")
        assertNotNullAndEquals(second.ref, "feature/api-fix")
        val page = pipelines.pageInfo
        assertNotNullAndEquals(page.startCursor, "cursor-start-abc")
        assertNotNullAndEquals(page.hasNextPage, true)
        assertNotNullAndEquals(page.endCursor, "cursor-end-xyz")
        assertNotNullAndEquals(page.hasPreviousPage, false)

    }

    @Test
    fun getAllProjectsTest() = runTest {
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedAllProjects)
        )
        val result = repository.getAllProjects().first()
        val page = result.currentUser!!.projectMemberships!!.pageInfo
        assertNotNullAndEquals("eyJpZCI6IjEwMSJ9", page.endCursor!!)
        assertNotNullAndEquals(true, page.hasNextPage)
        assertNotNullAndEquals(false, page.hasPreviousPage)
        assertNotNull(result)
        assertNotNull(result.currentUser.projectMemberships.nodes)
        assertFalse(result.currentUser.projectMemberships.nodes!!.isEmpty())
        assertEquals(1, result.currentUser.projectMemberships.nodes.size)
        val firstProject = result.currentUser.projectMemberships.nodes.first()
        assertEquals("gid://gitlab/ProjectMember/1", firstProject?.id)
        assertEquals("2023-10-27T10:00:00Z", firstProject?.createdAt)
        assertEquals("gid://gitlab/Project/101", firstProject?.project?.id)
        assertEquals("android-dev/gitlab-client", firstProject?.project?.fullPath)
        assertEquals(
            "A native Android client for GitLab built with Compose.",
            firstProject?.project?.description
        )
        assertEquals(
            "https://gitlab.com/uploads/project/avatar/101/logo.png",
            firstProject?.project?.avatarUrl
        )
        assertEquals("GitLab Client", firstProject?.project?.name)
        assertEquals("public", firstProject?.project?.visibility)
        val recentPipeline = firstProject!!.project!!.pipelines!!.nodes!![0]
        assertEquals("gid://gitlab/Ci::Pipeline/5001", recentPipeline!!.id)
        assertEquals(PipelineStatusEnum.SUCCESS, recentPipeline.status)
        val firstTopic = firstProject.project.topics!![0]
        assertEquals("android", firstTopic)
        val secondTopic = firstProject.project.topics[1]
        assertEquals("kotlin", secondTopic)
        val thirdTopic = firstProject.project.topics[2]
        assertEquals("graphql", thirdTopic)
        val firstLanguage = firstProject.project.languages!![0].name
        assertEquals("Kotlin", firstLanguage)
        val secondLanguage = firstProject.project.languages[1]
        assertEquals("Java", secondLanguage.name)
        // val recordedRequest = mockWebserver.takeRequest()
        //assertEquals("/graphql", recordedRequest.path)
    }

    @Test
    fun getProjectMembers() = runTest {
        val projectId = "gid://gitlab/Project/101"
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedMembers)
        )
        val data = repository.getProjectMembers(projectId).first()
        val members = data.project?.projectMembers
        assertNotNull(members)
        val page = members?.pageInfo
        assertNotNullAndEquals(page?.endCursor, "eyJpZCI6IjIifQ")
        assertNotNullAndEquals(page?.startCursor, "eyJpZCI6IjEifQ")
        assertNotNullAndEquals(page?.hasPreviousPage, false)
        assertNotNullAndEquals(page?.hasNextPage, true)
        val nodes = members?.nodes
        assertNotNull(nodes)
        assertFalse(nodes?.size == 0)
        ////////////////////////////////////////////////////////////
        val one = nodes!![0]
        assertNotNullAndEquals(one?.id, "gid://gitlab/ProjectMember/1")
        assertNotNullAndEquals(one?.createdAt, "2023-10-27T10:00:00Z")
        val user1 = one?.user
        assertNotNull(user1)
        assertNotNullAndEquals(user1?.name, "Lahcen AHTAT")
        assertNotNullAndEquals(user1?.username, "Ahtat204")
        assertNotNullAndEquals(user1?.bio, "Android Engineer & Systems Architecture enthusiast.")
        //////////////////////////////////////////////////////////////////////
        val two = nodes[1]
        assertNotNullAndEquals(two?.id, "gid://gitlab/ProjectMember/2")
        assertNotNullAndEquals(two?.createdAt, "2023-11-01T08:15:22Z")
        val user2 = two?.user
        assertNotNull(user2)
        assertNotNullAndEquals(user2?.name, "GitLab Bot")
        assertNotNullAndEquals(user2?.username, "project_101_bot")
        assertNull(user2?.bio)

    }

    @Test
    fun getProjectMergeRequest() = runTest {
        val projectId = "gid://gitlab/Project/101"
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedMrs)
        )
        val data = repository.getProjectMergeRequests(projectId).first()
        val mrs = data.project?.mergeRequests
        assertNotNull(mrs)
        val page = mrs?.pageInfo
        assertNotNull(page)
        assertNotNullAndEquals(page?.endCursor, "eyJpZCI6IjEyMzQ1In0")
        assertNotNullAndEquals(page?.startCursor, "eyJpZCI6IjEyMzQ1In0")
        assertNotNullAndEquals(page?.hasNextPage, false)
        assertNotNullAndEquals(page?.hasPreviousPage, false)
        val nodes = mrs?.nodes
        assertNotNull(nodes)
        nodes!!
        assertFalse(nodes.isEmpty())
        val one = nodes[0]
        assertNotNull(one)
        one!!
        assertNotNullAndEquals(one.name, "Features/implement-auth-flow")
        assertNotNullAndEquals(one.iid, "42")

        assertNotNullAndEquals(one.createdAt, "2026-10-04T21:40:00Z")
        assertNotNullAndEquals(one.conflicts, false)
        assertNotNullAndEquals(one.id, "gid://gitlab/MergeRequest/12345")
        assertNotNullAndEquals(one.updatedAt, "2026-10-04T21:45:00Z")
        assertNotNullAndEquals(one.state, MergeRequestState.opened)
        assertNotNullAndEquals(one.targetBranch, "main")
        assertNotNullAndEquals(one.sourceBranch, "features/implement-auth-flow")
        //labels
        val labels = one.labels
        assertNotNull(labels)
        labels!!
        val labelNodes = labels.nodes
        assertNotNull(labelNodes)
        labelNodes!!
        assertFalse(labelNodes.isEmpty())
        val firstLabel = labelNodes[0]
        assertNotNull(firstLabel)
        firstLabel!!
        assertNotNullAndEquals(firstLabel.id, "gid://gitlab/Label/789")
        assertNotNullAndEquals(firstLabel.title, "bugfix")
        assertNotNullAndEquals(firstLabel.color, "#FF0000")
        //pipelines
        val pipelines = one.pipelines?.edges
        assertNotNull(pipelines)
        pipelines!!
        val firstPipeline = pipelines[0]
        assertNotNull(firstPipeline)
        firstPipeline!!
        val firstPipelineNode = firstPipeline.node
        assertNotNull(firstPipelineNode)
        firstPipelineNode!!
        assertNotNullAndEquals(firstPipelineNode.id, "gid://gitlab/Ci::Pipeline/98765")
        assertNotNullAndEquals(firstPipelineNode.status, PipelineStatusEnum.SUCCESS)
        // author
        val author = one.author
        assertNotNull(author)
        author!!
        val authorName = author.name
        assertNotNullAndEquals(authorName, "Jane Doe")


    }
}
