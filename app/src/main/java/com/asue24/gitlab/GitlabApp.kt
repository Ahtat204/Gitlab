package com.asue24.gitlab

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GitlabApp : Application() {
    val context: Context by lazy { this }
}