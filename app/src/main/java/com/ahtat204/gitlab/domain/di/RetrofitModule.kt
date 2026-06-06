package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.repositories.stats.RetrofitClient
import com.ahtat204.gitlab.domain.usecase.authentication.constants.AuthConfig.REST_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(ViewModelComponent::class)
@Module
class RetrofitModule {
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): RetrofitClient {
        val gson = GsonBuilder().setLenient().create()
        val module = Retrofit.Builder().baseUrl(REST_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()
            .create(RetrofitClient::class.java)
        return module
    }
}