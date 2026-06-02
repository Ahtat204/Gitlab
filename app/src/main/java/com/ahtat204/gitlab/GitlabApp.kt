package com.ahtat204.gitlab

import android.app.Application
import com.ahtat204.gitlab.domain.usecase.authentication.constants.GlobalSingleton
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GitlabApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalSingleton.initialize(this)
    }
}