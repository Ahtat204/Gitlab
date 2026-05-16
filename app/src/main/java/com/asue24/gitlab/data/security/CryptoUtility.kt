package com.asue24.gitlab.data.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
/**
 * Utility object for encrypting and decrypting sensitive data using
 * the Android Keystore system.
 *
 * This class provides AES encryption with CBC block mode and PKCS7 padding,
 * ensuring that authentication tokens and other sensitive information are
 * securely persisted. Keys are generated and stored in the Android Keystore,
 * which prevents direct access to raw key material.
 *
 * ## Security Model
 * - Keys are generated with [KeyGenParameterSpec] and stored under the alias
 *   `secret` in the Android Keystore.
 * - AES/CBC/PKCS7 is used for encryption and decryption.
 * - A random initialization vector (IV) is generated for each encryption
 *   operation and prepended to the ciphertext for later decryption.
 * - Keys are created only once and reused for subsequent operations.
 *
 * ## Usage
 * - Call [encrypt] to securely encrypt a byte array. The returned data contains
 *   the IV followed by the ciphertext.
 * - Call [decrypt] to restore the original byte array from encrypted data.
 *
 * Example:
 * ```kotlin
 * val encrypted = CryptoUtility.encrypt("secret-data".toByteArray())
 * val decrypted = CryptoUtility.decrypt(encrypted)
 * val original = decrypted.decodeToString()
 * ```
 */
object CryptoUtility {
    private const val KEY_ALIAS = "secret"
    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
    private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
    private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

    private val cipher = Cipher.getInstance(TRANSFORMATION)
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    /**
     * Retrieves the existing secret key from the Android Keystore,
     * or generates a new one if none exists.
     *
     * @return A [SecretKey] used for encryption and decryption.
     */
    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    /**
     * Creates a new AES key and stores it in the Android Keystore.
     *
     * @return The newly generated [SecretKey].
     */
    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setRandomizedEncryptionRequired(true)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
        }.generateKey()
    }

    /**
     * Encrypts the given byte array using AES/CBC/PKCS7.
     *
     * A random IV is generated and prepended to the ciphertext.
     *
     * @param bytes The plain data to encrypt.
     * @return A byte array containing the IV followed by the ciphertext.
     */
    fun encrypt(bytes: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(bytes)
        return iv + encrypted
    }

    /**
     * Decrypts the given byte array using AES/CBC/PKCS7.
     *
     * The IV is extracted from the beginning of the byte array.
     *
     * @param bytes The encrypted data containing IV + ciphertext.
     * @return The decrypted plain data.
     */
    fun decrypt(bytes: ByteArray): ByteArray {
        val iv = bytes.copyOfRange(0, cipher.blockSize)
        val data = bytes.copyOfRange(cipher.blockSize, bytes.size)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        return cipher.doFinal(data)
    }
}
