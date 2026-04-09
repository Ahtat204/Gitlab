package com.asue24.gitlab.data.remote
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
class ApolloService  {
    companion object{
        fun setUpApolloClient(): ApolloClient {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttp = OkHttpClient
                .Builder()
                .addInterceptor(logging)
            return ApolloClient.Builder()
                .serverUrl("https://gitlab.com/api/graphql")
                .okHttpClient(okHttp.build())
                .build()
        }
    }
}