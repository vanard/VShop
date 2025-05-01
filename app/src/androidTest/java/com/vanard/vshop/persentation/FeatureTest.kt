package com.vanard.vshop.persentation

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vanard.data.di.DatabaseModule
import com.vanard.data.di.RepositoryModule
import com.vanard.feature.detail.DetailScreenTestTag
import com.vanard.feature.home.HomeScreenTestTag
import com.vanard.vshop.persentation.onboard.OnboardScreenTestTag
import com.vanard.vshop.util.fakeProductList
import com.vanard.vshop.util.waitUntilTimeout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(RepositoryModule::class, DatabaseModule::class)
class FeatureTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun performGoToHome() {
        with(composeRule) {
            onNodeWithTag(OnboardScreenTestTag.ONBOARD_TEXT).assertIsDisplayed()
            onNodeWithTag(OnboardScreenTestTag.LOGIN_BUTTON).assertIsDisplayed()
            onNodeWithTag(OnboardScreenTestTag.LOGIN_BUTTON).performClick()
            waitForIdle()
            onNodeWithTag(OnboardScreenTestTag.GUEST_BUTTON).assertIsDisplayed()
            onNodeWithTag(OnboardScreenTestTag.GUEST_BUTTON).performClick()
        }
    }

    @Test
    fun test_productListSuccess() {
        performGoToHome()
        with(composeRule) {
            waitUntilTimeout(500)
            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")
            waitUntilTimeout(1500)
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(0)
                .assert(hasText(fakeProductList.first().title))
        }
    }

    @Test
    fun test_productListSuccess_productDetailsSuccess() {
        val productTest = fakeProductList[1]
        performGoToHome()
        with(composeRule) {
            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")
            waitUntilTimeout(1500)
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(1)
                .assert(hasTestTag(productTest.title))
            onNodeWithTag(productTest.title).performClick()
            waitForIdle()
            onNodeWithText(productTest.title).assertIsDisplayed()
            onNodeWithTag(DetailScreenTestTag.ARROW_BACK).assertExists().performClick()

        }

    }

    @Test
    fun test_favorite() {
        val productTest = fakeProductList[2]
        performGoToHome()
        with(composeRule) {
            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")
            waitUntilTimeout(1500)
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(2)
                .assert(hasTestTag(productTest.title))
            onNodeWithTag(productTest.title).performClick()
            waitUntilTimeout(1000)
            onNodeWithTag(DetailScreenTestTag.FAV).assertIsDisplayed()
            onNodeWithTag(DetailScreenTestTag.FAV).performClick()
            onNodeWithTag(DetailScreenTestTag.ARROW_BACK).assertIsDisplayed()
            onNodeWithTag(DetailScreenTestTag.ARROW_BACK).performClick()
            waitForIdle()
            onNodeWithTag(productTest.title).assertIsDisplayed()
        }
    }

    @Test
    fun test_add_cart() {
        val productTest = fakeProductList[3]
        performGoToHome()
        with(composeRule) {
            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")
            waitUntilTimeout(1500)
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(3)
                .assert(hasTestTag(productTest.title))
            onNodeWithTag(productTest.title).performClick()
            waitUntilTimeout(1000)
            onNodeWithTag(DetailScreenTestTag.ADD_CART).assertIsDisplayed()
            onNodeWithTag(DetailScreenTestTag.ADD_CART).performClick()
            onNodeWithTag(DetailScreenTestTag.ARROW_BACK).assertIsDisplayed()
            onNodeWithTag(DetailScreenTestTag.ARROW_BACK).performClick()
            waitForIdle()
            onNodeWithTag(productTest.title).assertIsDisplayed()
        }
    }


}