package com.vanard.vshop.common

import android.content.Context
import android.widget.Toast

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}