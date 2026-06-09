package com.ahtat204.gitlab.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery
import com.ahtat204.gitlab.data.queries.GetProjectCommitsQuery.Node
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.FetchPolicy
import com.apollographql.apollo.cache.normalized.fetchPolicy
import javax.inject.Inject

typealias Commits = Node

class CommitsPagination @Inject constructor(private val apolloClient: ApolloClient) :
    PagingSource<Int, Node>() {
    private var projectId: String? = null
    fun setProjectId(id: String) {
        projectId = id
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Node> {
        val pageIndex = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val responseData = apolloClient.query(GetProjectCommitsQuery(projectId!!))
                .fetchPolicy(FetchPolicy.CacheFirst).execute().data?.project?.repository?.commits!!
            val nodes = responseData.nodes!! as List<Node>
            nodes.let {
                LoadResult.Page(
                    data = it,
                    prevKey = if (pageIndex == 1) null else pageIndex - 1,
                    nextKey = if (it.isEmpty()) null else pageIndex + 1
                )
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Commits>): Int? {
        TODO("Not yet implemented")
    }
}