package com.ahtat204.gitlab.domain.logging

import android.util.Log
import android.widget.Toast
import com.ahtat204.gitlab.domain.authentication.constants.Tokens.context

fun logger(message: String?, tag: String? = null) {
//    val thread =
//        Thread.currentThread().stackTrace[1].toString() + Thread.currentThread().stackTrace[3].toString()
    if (tag == null) Toast.makeText(context, message ?: "an error occurred", Toast.LENGTH_SHORT)
        .show();
    Log.d(
        tag ?: "com.ahtat204.gitlab.logger",
        message ?: "an error occurred"
    )

    //val logger =   OpenTelemetryRum.noop().getOpenTelemetry().logsBridge.loggerBuilder("Gitlab").build()
    //logger.logRecordBuilder().
}

/*
fun Logger(message: String?, tag: String? = null) {
    val thread = Thread.currentThread().stackTrace[2]
    Log.d(
        "com.ahtat204.gitlab.logger",
        message ?: "error occurred ,see ${thread.fileName}:${thread.lineNumber}"
    )
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(context, "logger: $message}", Toast.LENGTH_LONG).show()
    }
}*/
