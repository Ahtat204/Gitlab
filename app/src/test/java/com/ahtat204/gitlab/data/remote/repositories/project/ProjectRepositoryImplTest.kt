package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.Responses
import com.apollographql.apollo.ApolloClient
import okhttp3.mockwebserver.MockWebServer

class ProjectRepositoryImplTest : Responses() {
    private lateinit var mockWebserver: MockWebServer
    private lateinit var apolloClient: ApolloClient
    private lateinit var repository: ProjectRepository
/*
    @Before
    fun setUp() = runBlocking {
        mockWebserver = MockWebServer()
        mockWebserver.start()
        apolloClient =
            ApolloClient.Builder().serverUrl(mockWebserver.url("/graphql").toString()).build()
        repository = ProjectRepositoryImpl(apolloClient)
    }

    @After
    fun tearDown() {
        mockWebserver.shutdown()
    }

    @Test
    fun getProjectById() = runBlocking {
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedProject)
        )
        val response =
            apolloClient.query(GetProjectDetailsQuery("gid://gitlab/Project/123")).execute()
        assertNotNull(response)
        assertNull(response.errors)
        var data = response.data
        assertNotNull(data)
        assertEquals(data?.project?.id, "gid://gitlab/Project/123")
        repository.getProjectById("gid://gitlab/Project/123").collect { data = it }
        assertNotNull(data)
    }

    @Test
    fun getAllProjects() = runBlocking {
        mockWebserver.enqueue(
            MockResponse().setResponseCode(200).setBody(mockedProjects)
        )
        val response = apolloClient.query(GetMyPersonalProjectsQuery()).execute()
        assertNotNull(response)
        assertNull(response.errors)
        var data = response.data
        assertNotNull(data)
        repository.getAllProjects().collect { data = it }
        assertNotNull(data)
    }*/
}