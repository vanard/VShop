package com.vanard.common.util

import android.content.Context
import android.widget.Toast
import retrofit2.HttpException
import retrofit2.Response

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

suspend fun <T> Response<T>.safeBodyOrThrow(): T {
    if (isSuccessful) {
        return body() ?: throw NullPointerException("Response body is null")
    } else {
        throw HttpException(this)
    }
}