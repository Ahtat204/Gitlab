package com.ahtat204.gitlab.data.remote

import android.net.http.HttpException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery.Commits
import com.apollographql.apollo.ApolloClient
import okio.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CommitsPagination @Inject constructor(private val apolloClient: ApolloClient) :
    RemoteMediator<Int, Commits>() {
    private var _projectPath: String? = null
    public fun setProjectPath(path: String) {
        _projectPath = path
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Commits>
    ): MediatorResult {
        if(_projectPath==null) return MediatorResult.Error(Exception("you must specify the project you want to load its commits,call CommitsPagination::setProjectPath(path: String) "))
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        lastItem?.pageInfo.startCursor.toInt()
                    }
                }
            }
            val commits = apolloClient.query(GetProjectCommitsQuery(_projectPath!!))
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}