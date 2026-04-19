package com.asue24.gitlab

import android.app.Application
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import com.asue24.gitlab.data.repositories.AuthenticationRepository

class GitlabApp: Application() {
    val authRepo by lazy {
        AuthenticationRepository(this)
    }
}