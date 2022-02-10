package com.gsw.mvvmjetpackcompose

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addingItemsWorksCorrectly() {
        composeTestRule.setContent {
            App()
        }
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertExists()

        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 1 LastName 1").assertExists()
    }

    @Test
    fun removeItemsWorksCorrectly() {
        composeTestRule.setContent {
            App()
        }
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertExists()

        composeTestRule.onNodeWithContentDescription("Remove").performClick()
        composeTestRule.onNodeWithText("Name 0 LastName 0").assertDoesNotExist()
    }

//    @Test
//    fun loadingIsVisibleWhenAddingNewItem() {
//        composeTestRule.setContent {
//            App()
//        }
//        composeTestRule.onNodeWithContentDescription("Add").performClick()
//        composeTestRule.onNodeWithTag("loadingCard").assertExists()
//    }
}