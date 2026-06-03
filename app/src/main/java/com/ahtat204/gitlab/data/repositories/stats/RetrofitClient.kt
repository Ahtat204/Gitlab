package com.ahtat204.gitlab.data.repositories.stats

import com.ahtat204.gitlab.data.remote.retrofit.Events
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitClient {
    @GET("/events")
    fun getProjectsCount(): Response<Events>
}