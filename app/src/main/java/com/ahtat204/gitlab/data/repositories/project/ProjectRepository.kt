package com.ahtat204.gitlab.data.repositories.project

import com.ahtat204.gitlab.data.queries.GetMyProjectsPaginatedQuery
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun getAllProjects(): Flow<GetMyProjectsPaginatedQuery.Data>
    suspend fun getProjectById(id: String): Flow<GetProjectDetailsQuery.Data?>
    suspend fun getProjectCommits(
        id: String,
        offset: com.apollographql.apollo.api.Optional<String?> = Optional.absent()
    ): Flow<GetProjectCommitsQuery.Data?>

}