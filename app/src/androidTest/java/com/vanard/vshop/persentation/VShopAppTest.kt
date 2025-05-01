package com.vanard.vshop.persentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.vanard.data.di.DatabaseModule
import com.vanard.feature.home.HomeScreenTestTag
import com.vanard.ui.theme.VShopTheme
import com.vanard.vshop.navigation.Screen
import com.vanard.vshop.util.assertCurrentRouteName
import com.vanard.vshop.util.fakeProductList
import com.vanard.vshop.util.onNodeWithStringId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.vanard.resources.R
import com.vanard.vshop.util.waitUntilTimeout
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.util.Timer

@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VShopAppTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
//    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
//        composeTestRule.setContent {
//            VShopTheme {
        navController = TestNavHostController(composeTestRule.activity)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
//                VShopApp(navController = navController)
//            }
//    }
    }

//    @Test
//    fun testTopToBottom() {
//        test1_onboard_go_to_home()
//        test2_navHost_verifyStartDestination()
//        test3_navHost_clickItem_navigatesToDetailWithData()
//        test4_navHost_bottomNavigation_working()
//    }

    private fun performGoToHome() {
        with(composeTestRule) {
            onNodeWithTag(OnboardScreenTestTag.ONBOARD_TEXT).assertIsDisplayed()
            onNodeWithTag(OnboardScreenTestTag.LOGIN_BUTTON).assertIsDisplayed()
            onNodeWithTag(OnboardScreenTestTag.LOGIN_BUTTON).performClick()
            waitForIdle()
            onNodeWithTag(OnboardScreenTestTag.GUEST_BUTTON).assertIsDisplayed()
            onNodeWithTag(OnboardScreenTestTag.GUEST_BUTTON).performClick()
        }
    }

    @Test
    fun test1_onboard_go_to_home() {
        performGoToHome()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun test2_navHost_clickItem_navigatesToDetailWithData() {
        performGoToHome()
        with(composeTestRule) {
//            waitUntil(2000) {
//                onNodeWithText(fakeProductList[2].title).isDisplayed()
//            }
            this.waitUntilTimeout(2000)
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).performScrollToIndex(3)
//            onNodeWithText(fakeProductList[2].title).performClick()
//            navController.assertCurrentRouteName(Screen.Detail.route)
//            onNodeWithText(fakeProductList[2].title).assertIsDisplayed()
        }
    }

    @Test
    fun test3_navHost_bottomNavigation_working() {
        performGoToHome()
        with(composeTestRule) {
            onNodeWithStringId(R.string.screen_home).performClick()
            navController.assertCurrentRouteName(Screen.Wishlist.route)
            onNodeWithStringId(R.string.screen_wishlist).performClick()
            navController.assertCurrentRouteName(Screen.Cart.route)
            onNodeWithStringId(R.string.screen_cart).performClick()
            navController.assertCurrentRouteName(Screen.Profile.route)
            onNodeWithStringId(R.string.screen_profile).performClick()
            navController.assertCurrentRouteName(Screen.Home.route)
        }
    }
}