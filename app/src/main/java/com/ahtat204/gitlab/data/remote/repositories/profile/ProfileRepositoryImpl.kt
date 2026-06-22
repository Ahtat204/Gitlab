package com.ahtat204.gitlab.data.remote.repositories.profile

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.ahtat204.gitlab.domain.usecase.logging.logger
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

/**
 * Implementation of [ProfileRepository] that integrates with GitLab via Apollo GraphQL.
 *
 * ## Overview
 * - Provides reactive streams of user profile details using Kotlin [Flow].
 * - Uses Apollo’s normalized caching with configurable [FetchPolicy].
 * - Annotated with `@Inject` for dependency injection, ensuring a singleton lifecycle.
 *
 * ## Responsibilities
 * - Fetch a User's profile with various details ,such as profile description , job title,linked accounts,etc...
 * - Handle errors gracefully with logging and structured concurrency.
 *
 * ## Dependencies
 * - [ApolloClient]: Executes GraphQL queries and manages caching.
 * - [GetMyProfileQuery]: Auto‑generated query classes.
 * - Kotlin Coroutines Flow: Enables reactive, cancellable streams.
 */
@Singleton
class ProfileRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) :
    ProfileRepository {
    override fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data> {
        return apolloClient.query(GetMyProfileQuery()).fetchPolicy(policy).watch()
            .mapNotNull { it.data }.catch { ex ->
                logger("ProjectRepository", ex.cause.toString() + "\n" + ex.stackTrace)
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }
}
