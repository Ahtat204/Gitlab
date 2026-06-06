package com.ahtat204.gitlab.domain.di

import com.ahtat204.gitlab.data.repositories.stats.RetrofitClient
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier

@InstallIn(ViewModelComponent::class)
@Module
class RetrofitModule {
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): RetrofitClient {
    /*    val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }*/
        //val exclusionStrategy: ExclusionStrategy =
        val gson= GsonBuilder().setLenient()/*.excludeFieldsWithModifiers(Modifier.TRANSIENT).addDeserializationExclusionStrategy()*/.create()
        val module= Retrofit.Builder().baseUrl("https://gitlab.com/api/v4/")
            .addConverterFactory(GsonConverterFactory.create(gson))//.addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient).build().create(RetrofitClient::class.java)
        return module
    }
}