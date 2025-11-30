package com.vanard.domain.model

data class User(
    val id: String,
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

//val dummyUser = User(
//    id = "0192f5cd-0c2e-7a3f-b1d2-8b9a6d2b4c11",
//    email = "john.doe@example.com",
//    firstName = "John",
//    lastName = "Doe",
//    phone = "+1234567890",
//    address = Address(
//        street = "123 Main St",
//        city = "New York",
//        zipCode = "10001",
//        country = "USA"
//    )
//)

val dummyUser = User(
    id = "0192f5cd-0c2e-7a3f-b1d2-8b9a6d2b4c11",
    email = "demo@vshop.com",
    firstName = "Demo",
    lastName = "User",
    phone = "+1234567890",
    address = Address(
        street = "123 Main St",
        city = "New York",
        zipCode = "10001",
        country = "USA"
    )
)