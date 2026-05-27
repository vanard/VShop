package com.vanard.data.di

import okhttp3.Interceptor
import okhttp3.Response
import com.vanard.data.preferences.SecureUserPreferencesManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(
    private val securePrefs: SecureUserPreferencesManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = runBlocking {
            securePrefs.getAuthToken()
        }

        val requestBuilder = if (!token.isNullOrEmpty()) {
            original.newBuilder().addHeader("Authorization", "Bearer $token")
        } else {
            original.newBuilder()
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
