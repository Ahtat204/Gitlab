package com.asue24.gitlab.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.apollographql.apollo.network.okHttpClient
import com.asue24.gitlab.domain.utility.constants.Tokens
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApolloService {
    public fun setUpApolloClient(accessToken: String): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient.Builder().addInterceptor(logging)
        return ApolloClient.Builder().serverUrl("https://gitlab.com/api/graphql")
            .addHttpInterceptor(object : HttpInterceptor {
                override suspend fun intercept(
                    request: HttpRequest, chain: HttpInterceptorChain
                ): HttpResponse {
                    return chain.proceed(
                        request.newBuilder().addHeader("Authorization", "Bearer $accessToken").build()

                    )
                }
            }).okHttpClient(okHttp.build()).build()
    }
}
