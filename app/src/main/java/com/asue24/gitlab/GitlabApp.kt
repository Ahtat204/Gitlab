package com.asue24.gitlab

import android.app.Application
import com.asue24.gitlab.constants.AuthStorage

class GitlabApp: Application() {
    val authRepo by lazy {
        AuthenticationRepository(this, AuthStorage.getInstance(this))
    }
}