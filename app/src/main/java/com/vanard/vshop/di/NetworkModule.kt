package com.vanard.vshop.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.vanard.vshop.data.remote.ApiService
import com.vanard.vshop.network.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun getBaseUrl(): String = BASE_URL

    @Singleton
    @Provides
    fun getOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor(ChuckerInterceptor(context))
        okHttpClient.callTimeout(10, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(10, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(10, TimeUnit.SECONDS)
        okHttpClient.readTimeout(10, TimeUnit.SECONDS)
        val okHttp = okHttpClient.build()
        return okHttp
    }

//    @Provides
//    @Singleton
//    fun provideMoshiConverter(): MoshiConverterFactory {
//        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//
//        return
//    }

    @Singleton
    @Provides
    fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
//            .addConverterFactory(MoshiConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

//    @Singleton
//    @Provides
//    fun getApiProductService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

}