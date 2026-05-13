package com.vanard.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    @Embedded(prefix = "address_") val address: AddressEntity? = null
)