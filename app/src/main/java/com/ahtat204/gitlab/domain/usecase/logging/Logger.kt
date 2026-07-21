package com.ahtat204.gitlab.domain.usecase.logging

import android.util.Log
import android.widget.Toast
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context

fun logger( message: String?,tag: String?=null) {
    if(tag==null) Toast.makeText(context, message?:"an error occurred", Toast.LENGTH_SHORT).show();
    Log.d(tag?:"com.ahtat204.gitlab.logger", message?:"an error occurred")
}
fun Logger(message:String?,tag: String?=null){
    Log.d("com.ahtat204.gitlab.logger", message?:"error occurred")
    android.os.Handler(android.os.Looper.getMainLooper()).post {
        Toast.makeText(context, "logger: $message}", Toast.LENGTH_LONG).show()
    }
}