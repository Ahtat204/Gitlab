package com.asue24.gitlab

import android.app.Application
import com.asue24.gitlab.constants.AuthStorage

class GitlabApp: Application() {
    val authRepo=AuthenticationRepository(this, AuthStorage.getInstance(this))
}