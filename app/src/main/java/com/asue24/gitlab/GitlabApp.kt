package com.asue24.gitlab

import android.app.Application
import android.content.Context

class GitlabApp : Application() {
    val context: Context by lazy { this }
}