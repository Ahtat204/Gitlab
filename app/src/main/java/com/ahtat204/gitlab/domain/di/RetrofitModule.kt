package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.repositories.stats.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(): RetrofitClient {
        return Retrofit.Builder().baseUrl("https://gitlab.com/api/v4/").addConverterFactory(MoshiConverterFactory.create()).client(OkHttpClient()).build().create(RetrofitClient::class.java)
    }
}