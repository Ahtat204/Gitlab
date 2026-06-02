package com.ahtat204.gitlab.data.repositories.project

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProjectsQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class ProjectRepositoryImplTest {
    @Mock
    public lateinit var projectRepository: ProjectRepository

    @InjectMocks
    public lateinit var apolloClient: ApolloClient

    @Before
    fun setup() {
        apolloClient= Mockito.mock(ApolloClient::class.java)
        projectRepository = ProjectRepositoryImpl(apolloClient)
    }

    val currentUser = GetMyProjectsQuery.CurrentUser(
        "",
        "1",
        "gitlab.com",
        projectMemberships = null,
    )

    @Test
    fun GetAllProjectsTest() = runBlocking {
        var response: GetMyProjectsQuery.CurrentUser
        Mockito.`when`(
            apolloClient.query(GetMyProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapNotNull { it.data }.catch { ex ->
                Log.e("ProjectRepository", ex.cause.toString() + "\n" + ex.stackTrace)
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }).thenReturn(flowOf(GetMyProjectsQuery.Data(currentUser)))
        val result = projectRepository.getAllProjects(FetchPolicy.CacheFirst)
        Assert.assertNotNull(result)
        Assert.assertNotNull(result.single().currentUser)
        response = result.single().currentUser!!
        Assert.assertNotNull(response)
    }
}