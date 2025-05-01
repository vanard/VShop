package com.vanard.vshop.persentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.vanard.data.di.DatabaseModule
import com.vanard.data.di.RepositoryModule
import com.vanard.feature.home.HomeScreenTestTag
import com.vanard.vshop.persentation.onboard.OnboardScreenTestTag
import com.vanard.vshop.util.fakeProductList
import com.vanard.vshop.util.waitUntilTimeout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@UninstallModules(RepositoryModule::class, DatabaseModule::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VShopAppTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController
//    private lateinit var contextApplication: Context

    @Before
    fun setUp() {
        hiltRule.inject()
//        contextApplication = ApplicationProvider.getApplicationContext()
        navController = TestNavHostController(composeTestRule.activity).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }

        composeTestRule.activity.apply {
            navControllerProvider = { navController }
        }
    }

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
        val titleProduct = fakeProductList[5].title
        performGoToHome()
        with(composeTestRule) {
//            Log.d(TAG, "test2_navHost_clickItem_navigatesToDetailWithData: ${navController.currentBackStackEntry?.destination?.route}")
//            Log.d(TAG, "test2_navHost_clickItem_navigatesToDetailWithData: ${navController.currentDestination?.route}")
            this.waitUntilTimeout(1500)
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).performScrollToIndex(5)
            onNodeWithText(titleProduct).performClick()
//            this.waitUntilTimeout(1000)
            waitForIdle()
//            Log.d(TAG, "test2_navHost_clickItem_navigatesToDetailWithData: ${navController.currentBackStackEntry?.destination?.route}")
//            Log.d(TAG, "test2_navHost_clickItem_navigatesToDetailWithData: ${navController.currentDestination?.route}")
//            navController.assertCurrentRouteName(Screen.Detail.route)
            onNodeWithText(titleProduct).assertIsDisplayed()
        }
    }

//    @Test
//    fun test3_navHost_bottomNavigation_working() {
//        performGoToHome()
//        with(composeTestRule) {
//            this.waitUntilTimeout(1500)
////            onNodeWithStringId(composeTestRule.activity, R.string.screen_home).performClick()
//            onNodeWithStringId(R.string.screen_home).performClick()
//            navController.assertCurrentRouteName(Screen.Wishlist.route)
//            onNodeWithStringId(R.string.screen_wishlist).performClick()
//            navController.assertCurrentRouteName(Screen.Cart.route)
//            onNodeWithStringId(R.string.screen_cart).performClick()
//            navController.assertCurrentRouteName(Screen.Profile.route)
//            onNodeWithStringId(R.string.screen_profile).performClick()
//            navController.assertCurrentRouteName(Screen.Home.route)
//        }
//    }
}