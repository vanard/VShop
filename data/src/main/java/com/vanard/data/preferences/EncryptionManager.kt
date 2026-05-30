package com.vanard.data.preferences

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptionManager @Inject constructor(
    private val context: Context
) {
    private val keySetHandle: KeysetHandle by lazy {
        AeadConfig.init()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset_prefs", "master_key")
            .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
            .build()
            .keysetHandle
    }

    private val aead: Aead by lazy {
        keySetHandle.getPrimitive(Aead::class.java)
    }

    fun encrypt(plainText: String?): String? {
        if (plainText == null) return null
        return try {
            val cipherText = aead.encrypt(plainText.encodeToByteArray(), null)
            android.util.Base64.encodeToString(cipherText, android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    fun decrypt(encryptedText: String): String {
        val cipherText = android.util.Base64.decode(encryptedText, android.util.Base64.DEFAULT)
        val plainText = aead.decrypt(cipherText, null)
        return String(plainText)
    }

    fun decryptOrNull(encryptedText: String?): String? {
        if (encryptedText == null) return null
        return try {
            decrypt(encryptedText)
        } catch (e: Exception) {
            null
        }
    }
}
