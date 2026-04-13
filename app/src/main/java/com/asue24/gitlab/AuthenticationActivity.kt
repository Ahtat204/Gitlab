package com.asue24.gitlab

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asue24.gitlab.ui.theme.GitlabTheme

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val clientId = ""
            val redirectUrl = "com.asue24.gitlab://oauth2redirect"
            val scopes="read_api read_user read_repository"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gitlab.com/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUrl&&response_type=code&&scope=&scope=$scopes"))
            setContent {
                Button(onClick={startActivity(intent)},modifier = Modifier.size(80.dp).offset(20.dp)) {
                    Text("Login with github", fontSize = 30.sp)
                }
                val tokens=intent.data
                if (tokens!=null){
                    val code=tokens.getQueryParameter("code")
                    val state=tokens.getQueryParameter("state")
                    Log.d(tokens.toString(),code.toString())
                }
            }
        }
    }
}

