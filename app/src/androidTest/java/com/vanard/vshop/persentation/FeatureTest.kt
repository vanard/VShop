package com.vanard.vshop.persentation

import androidx.compose.ui.platform.LocalContext
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
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vanard.data.di.DatabaseModule
import com.vanard.domain.usecase.ProductUseCase
import com.vanard.feature.detail.DetailScreenTestTag
import com.vanard.feature.home.HomeScreenTestTag
import com.vanard.ui.theme.VShopTheme
import com.vanard.vshop.navigation.Screen
import com.vanard.vshop.repository.FakeFailureRepositoryImpl
import com.vanard.vshop.repository.FakeFailureRepositoryImpl.Companion.errorMessage
import com.vanard.vshop.repository.FakeSuccessRepositoryImpl
import com.vanard.vshop.util.assertCurrentRouteName
import com.vanard.vshop.util.fakeProductList
import com.vanard.vshop.util.waitUntilTimeout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(DatabaseModule::class)
class FeatureTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
        fakeSuccessRepo = FakeSuccessRepositoryImpl()
        fakeFailureRepo = FakeFailureRepositoryImpl()
    }

    private lateinit var navController: TestNavHostController

    private lateinit var productUseCase: ProductUseCase

    private lateinit var fakeSuccessRepo: FakeSuccessRepositoryImpl
    private lateinit var fakeFailureRepo: FakeFailureRepositoryImpl

    @After
    fun tearDown() {
        fakeSuccessRepo.reset()
        fakeFailureRepo.reset()
    }

    private fun initSuccessUseCase() {
        productUseCase = ProductUseCase(fakeSuccessRepo)
    }

    private fun initFailureUseCase() {
        productUseCase = ProductUseCase(fakeFailureRepo)
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
        initSuccessUseCase()
        performGoToHome()
        with(composeRule) {
            waitUntilTimeout(500)
            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")

            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(0)
                .assert(hasText(fakeProductList.first().title.plus(0)))
        }
    }

//    @Test
//    fun test_productListFailure() {
//        initFailureUseCase()
//        performGoToHome()
//        with(composeRule) {
//            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
//            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")
//
//            onNodeWithText(errorMessage).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun test_productListSuccess_productDetailsSuccess() {
//        initSuccessUseCase()
//        performGoToHome()
//        with(composeRule) {
//            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
//            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("product")
//            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
//            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(0)
//                .assert(hasTestTag(fakeProductList.first().title.plus(0)))
//            onNodeWithTag(fakeProductList.first().title.plus(0)).performClick()
//            waitForIdle()
//            onNodeWithText(fakeProductList.first().title).assertIsDisplayed()
//
//        }
//
//    }
//
//    @Test
//    fun test_favorite() {
//        initSuccessUseCase()
//        performGoToHome()
//        with(composeRule) {
//            onNodeWithTag(HomeScreenTestTag.SEARCH).performClick()
//            onNodeWithTag(HomeScreenTestTag.SEARCH).performTextInput("mens")
//            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).assertIsDisplayed()
//            onNodeWithTag(HomeScreenTestTag.LAZY_LIST).onChildAt(0)
//                .assert(hasTestTag(fakeProductList.first().title.plus(0)))
//            onNodeWithTag(fakeProductList.first().title.plus(0)).performClick()
//
//            onNodeWithTag(DetailScreenTestTag.FAV).performClick()
//            onNodeWithText(DetailScreenTestTag.FAV).assertIsDisplayed()
//            onNodeWithTag(DetailScreenTestTag.ARROW_BACK).performClick()
//            navController.assertCurrentRouteName(Screen.Wishlist.route)
//            waitForIdle()
//            onNodeWithText(fakeProductList.first().title).assertIsDisplayed()
//        }
//    }


}