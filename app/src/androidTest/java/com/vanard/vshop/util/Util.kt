package com.vanard.vshop.util

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.navigation.NavController
import org.junit.Assert
import java.util.Timer
import java.util.TimerTask

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}

fun ComposeContentTestRule.waitUntilTimeout(
    timeoutMillis: Long
) {
    AsyncTimer.start(timeoutMillis)
    this.waitUntil(
        condition = { AsyncTimer.expired },
        timeoutMillis = timeoutMillis + 1000
    )
}

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        val timerTask = TimerTaskImpl {
            expired = true
        }
        Timer().schedule(timerTask, delay)
    }
}

class TimerTaskImpl(private val runnable: Runnable) : TimerTask() {

    override fun run() {
        runnable.run()
    }
}