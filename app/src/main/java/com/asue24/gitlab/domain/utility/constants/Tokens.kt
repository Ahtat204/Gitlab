package com.asue24.gitlab.domain.utility.constants

import androidx.datastore.core.Serializer
import com.asue24.gitlab.data.security.CryptoUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object Tokens {
    var accessToken: String? = null
}

@Serializable
data class GitlabRefreshToken(var refreshToken: String? = null)
object RefreshTokenSerializer : Serializer<GitlabRefreshToken> {
    override val defaultValue: GitlabRefreshToken
        get() = GitlabRefreshToken()

    override suspend fun readFrom(input: InputStream): GitlabRefreshToken = try {
        val encryptedBytes = withContext(Dispatchers.IO) { input.readBytes() }
        val decryptedBytes = CryptoUtility.decrypt(encryptedBytes)
        Json.decodeFromString(decryptedBytes.decodeToString())
    } catch (e: Exception) {
        defaultValue
    }

    override suspend fun writeTo(t: GitlabRefreshToken, output: OutputStream) {
        val json = Json.encodeToString(t)
        val encryptedBytes = CryptoUtility.encrypt(json.toByteArray())
        withContext(Dispatchers.IO) {
            output.use { it.write(encryptedBytes) }
        }
    }
}