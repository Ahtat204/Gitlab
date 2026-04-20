package com.asue24.gitlab.domain.Authentication.constants
object Tokens {
    var accessToken: String? = null
}
/*
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
}*/
