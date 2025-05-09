package com.vanard.common.util

import android.content.Context
import android.widget.Toast
import retrofit2.HttpException
import retrofit2.Response
import kotlin.math.roundToInt

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Double.formalDecimal() =
    (this * 100).roundToInt() / 100.0