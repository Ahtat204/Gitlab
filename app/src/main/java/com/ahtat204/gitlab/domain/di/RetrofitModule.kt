package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.repositories.stats.RetrofitClient
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(): RetrofitClient {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return Retrofit.Builder().baseUrl("https://gitlab.com/api/v4/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(OkHttpClient()).build().create(RetrofitClient::class.java)
    }
}