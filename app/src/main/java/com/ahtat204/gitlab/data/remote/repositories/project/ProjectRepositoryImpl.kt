package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.data.fetchAndMergeCommits
import com.ahtat204.gitlab.data.queries.GetMyPersonalProjectsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.ahtat204.gitlab.data.queries.GetProjectPipelinesQuery
import com.ahtat204.gitlab.data.queries.GetProjectRepositoryQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryBranchesQuery
import com.ahtat204.gitlab.data.queries.GetRepositoryCommitsQuery
import com.ahtat204.gitlab.data.queries.type.PipelineStatusEnum
import com.ahtat204.gitlab.data.remote.repositories.mapAndHandleErrors
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.Optional
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ProjectRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Overview
 * - Provides reactive streams of project data using Kotlin [Flow].
 * - Uses Apollo’s normalized caching with configurable [FetchPolicy].
 * - Annotated with `@Inject` for dependency injection, ensuring a singleton lifecycle.
 * - Annotated with [Singleton] to avoid creating a new Repository everytime the ViewModel is created since this Dependency is just for fetching data , doesn't have a state to hold
 *
 * ## Responsibilities
 * - Fetch all projects contributed by the authenticated user.
 * - Retrieve repository tree data for a specific project by ID.
 * - Handle errors gracefully with logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * - [GetMyPersonalProjectsQuery], [GetProjectDetailsQuery],[GetProjectRepositoryQuery],[GetRepositoryCommitsQuery],[GetRepositoryBranchesQuery]: Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 * @author Lahcen AHTAT
 */
@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProjectRepository {
    @OptIn(ApolloExperimental::class)
    override suspend fun getAllProjects(): Flow<GetMyPersonalProjectsQuery.Data> =
        apolloClient.query(GetMyPersonalProjectsQuery()).fetchPolicy(FetchPolicy.CacheFirst).watch()
            .mapAndHandleErrors()

    override suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?> {
        return apolloClient.query(GetProjectDetailsQuery(id)).fetchPolicy(FetchPolicy.CacheFirst)
            .watch().mapAndHandleErrors()
    }

    override suspend fun getProjectCommits(
        id: String, branch: String, cursor: String?
    ): Flow<GetRepositoryCommitsQuery.Data?> {
        return apolloClient.query(
            GetRepositoryCommitsQuery(
                id, branch = branch, cursor = Optional.presentIfNotNull(cursor)
            )
        ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors().fetchAndMergeCommits(client = apolloClient, branch, id, cursor)

    }

    override suspend fun getProjectPipelines(
        project: String, cursor: String?, status: PipelineStatusEnum?
    ): Flow<GetProjectPipelinesQuery.Data> {
       return apolloClient.query(
            GetProjectPipelinesQuery(
                status = Optional.presentIfNotNull(status),
                project = project,
                cursor = Optional.presentIfNotNull(cursor)
            )
        ).fetchPolicy(
            FetchPolicy.CacheFirst
        ).watch().mapAndHandleErrors()

    }

    override suspend fun getRepositoryBranches(
        project: String, skip: Int
    ): Flow<GetRepositoryBranchesQuery.Data> {
        return apolloClient.query(GetRepositoryBranchesQuery(project, skip))
            .fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()
    }

    override suspend fun getProjectRepository(
        id: String, branch: String?, path: String?
    ): Flow<GetProjectRepositoryQuery.Data?> {
        return apolloClient.query(
            GetProjectRepositoryQuery(
                id,
                branch = Optional.presentIfNotNull(branch),
                path = Optional.presentIfNotNull(path)
            )
        ).fetchPolicy(FetchPolicy.CacheFirst).watch().mapAndHandleErrors()

    }
}
