package com.ahtat204.gitlab.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.instrumentation.okhttp.v3_0.OkHttpTelemetry
import okhttp3.Call
import okhttp3.OkHttpClient
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object OpenTelemetryModule {
    @Singleton
    @Provides
    fun providesCallFactory(okHttpClient: OkHttpClient): Call.Factory {
        return OkHttpTelemetry.builder(OpenTelemetry.noop()).build()
            .newCallFactory(okHttpClient);
    }
}