package com.ahtat204.gitlab.data.repositories.stats

import retrofit2.http.GET

interface AnalyticsRepository {
    @GET("/api/v4/projects")
    fun getProjectsCount()
}