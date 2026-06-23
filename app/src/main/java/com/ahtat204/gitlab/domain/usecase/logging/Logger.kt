package com.ahtat204.gitlab.domain.usecase.logging

import android.util.Log
import android.widget.Toast
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens.context

fun logger( message: String?,tag: String?=null) {
   message?.let {
       Log.d("com.ahtat204.gitlab.logger", it)
       Toast.makeText(context, message, Toast.LENGTH_LONG).show();
   }
}