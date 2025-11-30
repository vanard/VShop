package com.vanard.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vanard.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_FIRST_NAME = stringPreferencesKey("user_first_name")
        private val USER_LAST_NAME = stringPreferencesKey("user_last_name")
        private val USER_PHONE = stringPreferencesKey("user_phone")
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userId: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] ?: ""
    }

    private val userEmail: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL] ?: ""
    }

    private val userFirstName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_FIRST_NAME] ?: ""
    }

    private val userLastName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_LAST_NAME] ?: ""
    }

    private val userPhone: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_PHONE] ?: ""
    }

    private val authToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN] ?: ""
    }

    val userPrefsFlow: Flow<User> =
        combine(
            userId,
            userEmail,
            userFirstName,
            userLastName,
            userPhone,
        ) { userId, email, firstName, lastName, phone ->
            User(
                id = userId,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phone = phone.takeIf { it.isNotEmpty() }
            )
        }

    suspend fun saveUserSession(
        userId: String,
        email: String,
        firstName: String,
        lastName: String,
        phone: String? = null,
        token: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_FIRST_NAME] = firstName
            preferences[USER_LAST_NAME] = lastName
            phone?.let { preferences[USER_PHONE] = it }
            preferences[AUTH_TOKEN] = token
        }
    }

    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun updateUserInfo(
        firstName: String,
        lastName: String,
        phone: String? = null
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_FIRST_NAME] = firstName
            preferences[USER_LAST_NAME] = lastName
            phone?.let { preferences[USER_PHONE] = it }
        }
    }
}