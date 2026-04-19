package com.asue24.gitlab.domain.utility.constants

import com.asue24.gitlab.data.security.CryptoUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream

object AuthStateSerializer : androidx.datastore.core.Serializer<AuthState> {
    override val defaultValue = AuthState()
    override suspend fun readFrom(input: InputStream): AuthState {
        return withContext(Dispatchers.IO) {
            try {
                val encryptedBytes = input.readBytes()
                if (encryptedBytes.isEmpty()) return@withContext defaultValue
                val decryptedBytes = CryptoUtility.decrypt(encryptedBytes)
                val jsonString = decryptedBytes.decodeToString()
                AuthState.jsonDeserialize(jsonString)
            } catch (e: Exception) {
                defaultValue
                throw e
            }
        }
    }

    override suspend fun writeTo(t: AuthState, output: OutputStream) {
        withContext(Dispatchers.IO) {
            val jsonString = t.jsonSerializeString()
            val bytes = jsonString.toByteArray()
            val encryptedBytes = CryptoUtility.encrypt(bytes)
            output.write(encryptedBytes)
        }
    }
}