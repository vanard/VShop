package com.vanard.vshop.util

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithStringId(
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithText(activity.getString(id))

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.onNodeWithStringId(
    @StringRes id: Int, vararg args: Any,
): SemanticsNodeInteraction = onNodeWithText(activity.getString(id, args))

fun ComposeContentTestRule.onNodeWithStringId(
    context: Context,
    @StringRes id: Int
): SemanticsNodeInteraction = onNodeWithText(context.getString(id))

fun ComposeContentTestRule.onNodeWithStringId(
    context: Context,
    @StringRes id: Int,
    vararg args: Any,
): SemanticsNodeInteraction = onNodeWithText(context.getString(id, args))