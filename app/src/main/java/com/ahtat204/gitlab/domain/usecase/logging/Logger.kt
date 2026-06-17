package com.ahtat204.gitlab.domain.usecase.logging

import android.util.Log
import android.widget.Toast
import com.ahtat204.gitlab.domain.usecase.authentication.constants.Tokens

fun logger( message: String?,tag: String?=null) {
   message?.let {Log.d(tag?:"com.ahtat204.gitlab", it)
       Toast.makeText(Tokens.context, message, Toast.LENGTH_LONG).show();
   }
}