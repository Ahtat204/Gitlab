package com.asue24.gitlab.constants

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream

object AuthStateSerializer : Serializer<AuthState> {
    override val defaultValue = AuthState()

    override suspend fun readFrom(input: InputStream): AuthState {
        try {
            // Read the JSON string and deserialize it
            return AuthState.jsonDeserialize(input.bufferedReader().use { it.readText() })
        } catch (e: Exception) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: AuthState, output: OutputStream) {
        // Serialize the object to a JSON string and write it
        output.write(t.jsonSerializeString().toByteArray())
    }
}