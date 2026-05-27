package com.vanard.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_NAME = "secure_user_prefs"
private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")

private val Context.secureDataStore by preferencesDataStore(name = PREFS_NAME)

@Singleton
class SecureUserPreferencesManager @Inject constructor(
    private val context: Context,
    private val encryptionManager: EncryptionManager
) {
    suspend fun saveAuthToken(token: String) {
        val encryptedToken = encryptionManager.encrypt(token)
        context.secureDataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = encryptedToken
        }
    }

    suspend fun clearAuthToken() {
        context.secureDataStore.edit { prefs ->
            prefs.remove(KEY_AUTH_TOKEN)
        }
    }

    suspend fun getAuthToken(): String? {
        val encryptedToken = context.secureDataStore.data.map { prefs ->
            prefs[KEY_AUTH_TOKEN]
        }.first()

        return encryptedToken?.let {
            try {
                encryptionManager.decrypt(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}
