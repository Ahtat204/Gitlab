package com.asue24.gitlab.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.asue24.gitlab.domain.utility.constants.AuthStorage
import com.asue24.gitlab.presentation.activities.ui.theme.GitlabTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    lifecycleScope.launch {
    // Get the RefreshToken string from your DataStore
    val refreshToken = AuthStorage.getAuthState(this@LauncherActivity).data.first().refreshToken

    if (refreshToken.isNullOrEmpty()) {
        startActivity(Intent(this@LauncherActivity, AuthenticationActivity::class.java))
    } else {
        startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
    }
    finish()
}
    }
}


