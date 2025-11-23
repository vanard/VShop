package com.vanard.data.mappers

import com.vanard.data.entities.AddressEntity
import com.vanard.data.entities.UserEntity
import com.vanard.domain.model.Address
import com.vanard.domain.model.User

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