package com.vanard.common.util

import android.content.Context
import android.widget.Toast

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}