package com.ahtat204.gitlab.presentation.viewmodels

import com.ahtat204.gitlab.data.remote.repositories.graphql.GraphQlRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
open class TestBase {
    protected val repository: GraphQlRepository = mock()
    protected val testDispatcher = UnconfinedTestDispatcher()
}