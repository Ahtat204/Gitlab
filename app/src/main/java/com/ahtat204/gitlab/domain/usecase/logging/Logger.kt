package com.ahtat204.gitlab.domain.usecase.logging

import android.util.Log
import android.widget.Toast
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context

fun logger( message: String?,tag: String?=null) {
       Log.d(tag?:"com.ahtat204.gitlab.logger", message?:"an error occurred")
       Toast.makeText(context, message?:"an error occurred", Toast.LENGTH_LONG).show();
}