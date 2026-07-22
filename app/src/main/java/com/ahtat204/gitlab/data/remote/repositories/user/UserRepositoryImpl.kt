package com.ahtat204.gitlab.data.remote.repositories.user

import com.ahtat204.gitlab.data.queries.GetUserProjectsByNameQuery
import com.ahtat204.gitlab.data.remote.repositories.mapAndHandleErrors
import com.apollographql.apollo.ApolloClient
import com.apollographql.cache.normalized.FetchPolicy
import com.apollographql.cache.normalized.fetchPolicy
import com.apollographql.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [UserRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Overview
 * - Provides reactive streams of project data using Kotlin [Flow].
 * - Uses Apollo’s normalized caching with configurable [FetchPolicy].
 * - Annotated with `@Inject` for dependency injection, ensuring a singleton lifecycle.
 * - Annotated with `@Singleton` to avoid creating a new Repository everytime the ViewModel is created since this Dependency is just for fetching data , doesn't have a state to hold
 *
 * ## Responsibilities
 * - Fetch all projects contributed by a given User.
 * - Retrieve repository tree data for a specific project by ID.
 * - Handle errors gracefully with logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * - [GetUserProjectsByNameQuery]:Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 * @author Lahcen AHTAT
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : UserRepository {
    override suspend fun getUserProjectsByName(
        userName: String,
        policy: FetchPolicy
    ): Flow<GetUserProjectsByNameQuery.Data?> {
        return apolloClient.query(GetUserProjectsByNameQuery(userName)).fetchPolicy(policy).watch()
            .mapAndHandleErrors()
    }
}