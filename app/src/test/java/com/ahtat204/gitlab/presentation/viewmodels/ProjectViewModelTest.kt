package com.ahtat204.gitlab.presentation.viewmodels

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.testing.runTest
import com.apollographql.apollo3.testing.enqueueTestResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GetUserQueryTest {

    @Test
    fun `test GetUser query with mocked response`() = runTest {
        // Create a mock Apollo client
        val mockClient = ApolloClient.Builder()
            .serverUrl("https://mock.server/graphql") // URL won't be used
            .build()

        // Enqueue a fake response for the query
        mockClient.enqueueTestResponse(
            GetUserQuery(id = "123"),
            GetUserQuery.Data(
                user = GetUserQuery.User(
                    id = "123",
                    name = "John Doe"
                )
            )
        )

        // Execute the query
        val response = mockClient.query(GetUserQuery(id = "123")).execute()

        // Assertions
        assertNotNull(response.data)
        assertEquals("123", response.data!!.user?.id)
        assertEquals("John Doe", response.data!!.user?.name)
    }
}