package com.vanard.vshop.di

import com.vanard.vshop.data.remote.ApiInterface
import com.vanard.vshop.network.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun getBaseUrl(): String = BASE_URL

    @Singleton
    @Provides
    fun getRetrofitClient(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun getApiClient(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)

}