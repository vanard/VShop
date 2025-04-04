package com.vanard.vshop.data.remote

import kotlinx.coroutines.flow.Flow
import okhttp3.Route
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

const val PRODUCTS_BY_CATEGORY_ENDPOINT = "products/category/{category}"
const val PRODUCT_BY_ID_ENDPOINT = "products/{id}"
const val PRODUCTS_ENDPOINT = "products"

interface ApiInterface {

    @GET(PRODUCTS_ENDPOINT)
    suspend fun getAllProducts(): Flow<Response<ProductList>>

    @GET(PRODUCT_BY_ID_ENDPOINT)
    suspend fun getProductById(@Path(RouteConstant.ID) id: Int): Response<Product>

    @GET(PRODUCTS_BY_CATEGORY_ENDPOINT)
    suspend fun getProductsByCategory(@Path(RouteConstant.CATEGORY) category: String): Response<ProductList>

}