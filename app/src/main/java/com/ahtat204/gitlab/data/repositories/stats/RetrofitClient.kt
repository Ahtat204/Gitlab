package com.ahtat204.gitlab.data.repositories.stats

import retrofit2.http.GET

interface RetrofitClient {
    @GET("/events")
    fun getProjectsCount()
}