package com.vanard.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    @Embedded val address: AddressEntity? = null
)

data class AddressEntity(
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String
)