package com.vanard.domain.model.auth

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null
)

data class AuthResponse(
    val user: com.vanard.domain.model.User,
    val token: String
)