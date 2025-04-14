package com.vanard.data.remote

import com.vanard.data.remote.dto.CartDto
import retrofit2.Response
import retrofit2.http.GET

const val CARTS_ENDPOINT = "carts"

interface CartService {
    @GET(CARTS_ENDPOINT)
    suspend fun getAllCarts(): Response<List<CartDto>>
}