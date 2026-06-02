package com.ahtat204.gitlab.domain.usecase.authentication.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class CryptoUtilityTest {
    private lateinit var crypto: CryptoUtility

    @Before
    public fun setup() {
        crypto = mock(CryptoUtility)
    }

    @Test
    fun testEncryptionDecryption() {
        val testEncryption: ByteArray = "testing203".toByteArray()
        val encryptedByteArray = crypto.encrypt(testEncryption)
        assertNotNull(encryptedByteArray)
        assertNotEquals(testEncryption, encryptedByteArray)
        val decryptedByteArray = crypto.decrypt(encryptedByteArray)
        assertNotNull(decryptedByteArray)
        assertNotEquals(decryptedByteArray, encryptedByteArray)
        assertEquals(decryptedByteArray, testEncryption)

    }

    @Test
    fun decrypt() {
    }

}