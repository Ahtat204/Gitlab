package com.ahtat204.gitlab.domain.usecase.authentication.constants

import android.content.Context
import net.openid.appauth.AuthState

object GlobalSingleton {
    var accessToken: String? = null
    var CurrentAuthState: AuthState? = null
    private var appContext: Context? = null
    val context: Context
        get() = appContext
            ?: throw IllegalStateException("Tokens object must be initialized with Application Context first.")

    fun initialize(context: Context) {
        if (appContext == null) {
            appContext = context.applicationContext
        }
    }
}

