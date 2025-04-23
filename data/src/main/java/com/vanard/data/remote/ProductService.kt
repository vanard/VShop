package com.vanard.data.remote

import com.vanard.common.constants.RouteConstant
import com.vanard.data.remote.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

const val PRODUCTS_BY_CATEGORY_ENDPOINT = "products/category/{category}"
const val PRODUCT_BY_ID_ENDPOINT = "products/{id}"
const val PRODUCTS_ENDPOINT = "products"

interface ProductService {

    @GET(PRODUCTS_ENDPOINT)
    suspend fun getAllProducts(): Response<List<ProductDto>>

    @GET(PRODUCT_BY_ID_ENDPOINT)
    suspend fun getProductById(@Path(RouteConstant.ID) id: Long): Response<ProductDto>

    @GET(PRODUCTS_BY_CATEGORY_ENDPOINT)
    suspend fun getProductsByCategory(@Path(RouteConstant.CATEGORY) category: String): Response<List<ProductDto>>

}