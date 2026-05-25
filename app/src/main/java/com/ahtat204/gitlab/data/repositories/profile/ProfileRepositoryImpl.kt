package com.ahtat204.gitlab.data.repositories.profile

import android.util.Log
import com.ahtat204.gitlab.data.queries.GetMyProfileQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import com.apollographql.apollo.cache.normalized.watch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ProfileRepositoryImpl @Inject constructor(private val apolloClient: ApolloClient) :
    ProfileRepository {
    override fun getMyProfile(policy: FetchPolicy): Flow<GetMyProfileQuery.Data> {
        return apolloClient.query(GetMyProfileQuery()).fetchPolicy(policy).watch()
            .mapNotNull { it.data }.catch { ex ->
                Log.e("ProjectRepository", ex.cause.toString() + "\n" + ex.stackTrace)
                if (ex is CancellationException) throw ex
            }.mapNotNull { it }
    }
}
