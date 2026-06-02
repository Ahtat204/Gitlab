package com.ahtat204.gitlab.domain.usecase.authentication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.Serializer
import com.ahtat204.gitlab.data.security.CryptoUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer implementation for persisting and restoring [AuthState] objects
 * using Android DataStore. This serializer ensures that authentication state
 * is stored securely by encrypting serialized data before writing and
 * decrypting it when reading.
 *
 * The encryption and decryption are handled by [CryptoUtility], which provides
 * AES-based secure transformations. This guarantees that sensitive OAuth tokens
 * and session information are not stored in plain text.
 *
 * ## Usage
 * - Used by DataStore to automatically save and restore [AuthState].
 * - Provides a default empty [AuthState] when no data is available.
 *
 * ## Implementation details
 * - `readFrom`: Reads encrypted bytes from the input stream, decrypts them,
 *   and deserializes the JSON string into an [AuthState].
 * - `writeTo`: Serializes the given [AuthState] into JSON, encrypts the bytes,
 *   and writes them to the output stream.
 *
 * @property defaultValue The default [AuthState] returned when no persisted
 * data is available or when the input stream is empty.
 */
object AuthStateSerializer : Serializer<AuthState> {

    /**
     * Returns a default empty [AuthState] instance.
     */
    override val defaultValue: AuthState get() = AuthState()

    /**
     * Reads and deserializes an [AuthState] from the given [InputStream].
     *
     * The stored data is expected to be encrypted. If the stream is empty,
     * the [defaultValue] is returned.
     *
     * @param input The input stream containing encrypted [AuthState] data.
     * @return The deserialized [AuthState] object.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun readFrom(input: InputStream): AuthState {
        val encryptedBytes = input.readBytes()
        if (encryptedBytes.isEmpty()) return defaultValue
        val decryptedBytes = CryptoUtility.decrypt(encryptedBytes)
        val jsonString = decryptedBytes.decodeToString()
        return AuthState.jsonDeserialize(jsonString)
    }

    /**
     * Serializes and writes the given [AuthState] to the provided [OutputStream].
     *
     * The [AuthState] is first converted to JSON, then encrypted before being
     * persisted. This ensures sensitive authentication data is securely stored.
     *
     * @param t The [AuthState] to serialize and persist.
     * @param output The output stream where encrypted data will be written.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun writeTo(t: AuthState, output: OutputStream) {
        withContext(Dispatchers.IO) {
            val jsonString = t.jsonSerializeString()
            val encryptedBytes = CryptoUtility.encrypt(jsonString.toByteArray())
            output.write(encryptedBytes)
        }
    }
}
