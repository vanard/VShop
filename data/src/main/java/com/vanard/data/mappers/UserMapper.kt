package com.vanard.data.mappers

import com.google.firebase.auth.FirebaseUser
import com.vanard.data.entities.AddressEntity
import com.vanard.data.entities.UserEntity
import com.vanard.domain.model.Address
import com.vanard.domain.model.User
import com.vanard.domain.model.auth.AuthResponse

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        address = address?.toDomain()
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        address = address?.toEntity()
    )
}

fun AddressEntity.toDomain(): Address {
    return Address(
        street = street,
        city = city,
        zipCode = zipCode,
        country = country
    )
}

fun Address.toEntity(): AddressEntity {
    return AddressEntity(
        street = street,
        city = city,
        zipCode = zipCode,
        country = country
    )
}

// Helper extension to map FirebaseUser to AuthResponse (domain model)
fun FirebaseUser.toAuthResponse(): AuthResponse {
    val user = User(
        id = uid,
        email = email ?: "",
        firstName = displayName?.split(" ")?.firstOrNull() ?: "",
        lastName = displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
        phone = phoneNumber
    )
    val token = getIdToken(false).result?.token ?: ""
    return AuthResponse(user = user, token = token)
}