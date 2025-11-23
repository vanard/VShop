package com.vanard.domain.validation

/**
 * Validation utilities for authentication operations
 */
object AuthValidation {

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("Email cannot be empty")
            !email.contains("@") -> ValidationResult.Invalid("Invalid email format")
            !email.contains(".") -> ValidationResult.Invalid("Invalid email format")
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("Password cannot be empty")
            password.length < 6 -> ValidationResult.Invalid("Password must be at least 6 characters")
            else -> ValidationResult.Valid
        }
    }

    fun validateName(name: String, fieldName: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid("$fieldName cannot be empty")
            name.length < 2 -> ValidationResult.Invalid("$fieldName must be at least 2 characters")
            else -> ValidationResult.Valid
        }
    }

    fun validatePhone(phone: String?): ValidationResult {
        return when {
            phone.isNullOrBlank() -> ValidationResult.Valid // Phone is optional
            phone.length < 10 -> ValidationResult.Invalid("Phone number must be at least 10 digits")
            else -> ValidationResult.Valid
        }
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

fun ValidationResult.isValid(): Boolean = this is ValidationResult.Valid

fun ValidationResult.getErrorMessage(): String? =
    if (this is ValidationResult.Invalid) message else null