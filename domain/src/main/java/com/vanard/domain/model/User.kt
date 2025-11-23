package com.vanard.domain.model

data class User(
    val id: Long = 0,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val address: Address? = null
) {
    val fullName: String
        get() = "$firstName $lastName"
}

data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val country: String
) {
    val fullAddress: String
        get() = "$street, $city, $zipCode, $country"
}

val dummyUser = User(
    id = 1,
    email = "john.doe@example.com",
    firstName = "John",
    lastName = "Doe",
    phone = "+1234567890",
    address = Address(
        street = "123 Main St",
        city = "New York",
        zipCode = "10001",
        country = "USA"
    )
)