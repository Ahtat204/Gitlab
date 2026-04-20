package com.asue24.gitlab.domain.authentication.constants

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.Serializer
import com.asue24.gitlab.data.security.CryptoUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream

object AuthStateSerializer : Serializer<AuthState> {
    override val defaultValue: AuthState get() = AuthState()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun readFrom(input: InputStream): AuthState {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) return defaultValue
        val decryptedBytes = CryptoUtility.decrypt(encryptedBytes)
        val jsonString = decryptedBytes.decodeToString()
        return AuthState.jsonDeserialize(jsonString)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun writeTo(t: AuthState, output: OutputStream) {
        withContext(Dispatchers.IO) {
            val jsonString = t.jsonSerializeString()
            val encryptedBytes = CryptoUtility.encrypt(jsonString.toByteArray())
            output.write(encryptedBytes)
        }
    }
}