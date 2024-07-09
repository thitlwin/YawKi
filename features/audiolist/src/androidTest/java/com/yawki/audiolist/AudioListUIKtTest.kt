package com.yawki.audiolist

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.yawki.common.data.DataProvider
import com.yawki.common.presentation.PlayerUIState
import com.yawki.common.presentation.auth.AuthUiState
import com.yawki.common.utils.ContentDescriptions
import com.yawki.common.utils.TestTags
import org.junit.Rule
import org.junit.Test

class AudioListUIKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingStateDisplayed() {
        // Arrange: Set up the loading state
        composeTestRule.setContent {
            val mockState = AudioListUIState(loading = true)
            AudioListScreen(modifier = Modifier,
                state = mockState,
                selectedMonk = DataProvider.monks.first(),
                playerUIState = PlayerUIState(),
                authUiState = AuthUiState(),
                onPlayerEvent = {},
                onEvent = {},
                onSongClick = {},
                onSignIn = {}
            )
        }
        // Assert: Verify that the loading state is displayed
        composeTestRule.onNodeWithTag(TestTags.LOADING_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun testErrorStateDisplayed() {
        // Arrange: Set up the error state
        val errorText = "Test Error"
        composeTestRule.setContent {
            val mockState = AudioListUIState(error = Exception(errorText))
            AudioListScreen(modifier = Modifier,
                state = mockState,
                selectedMonk = DataProvider.monks.first(),
                playerUIState = PlayerUIState(),
                authUiState = AuthUiState(),
                onPlayerEvent = {},
                onEvent = {},
                onSongClick = {},
                onSignIn = {}
            )
        }

        composeTestRule.onNodeWithText(errorText).assertIsDisplayed()
    }

    @Test
    fun testEmptyStateDisplayed() {
        // Arrange: Set up the empty state
        composeTestRule.setContent {
            val mockState = AudioListUIState(songs = emptyList())
            AudioListScreen(modifier = Modifier,
                state = mockState,
                selectedMonk = DataProvider.monks.first(),
                playerUIState = PlayerUIState(),
                authUiState = AuthUiState(),
                onPlayerEvent = {},
                onEvent = {},
                onSongClick = {},
                onSignIn = {
                })
        }

        composeTestRule.onNodeWithTag(TestTags.EMPTY_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun testAudioListDisplayedWithSongs() {
        // Arrange: Set up the audio list
        composeTestRule.setContent {
            val mockState = AudioListUIState(songs = DataProvider.songs)
            AudioListScreen(modifier = Modifier,
                state = mockState,
                selectedMonk = DataProvider.monks.first(),
                playerUIState = PlayerUIState(),
                authUiState = AuthUiState(),
                onPlayerEvent = {},
                onEvent = {},
                onSongClick = {},
                onSignIn = {

                })
        }
        composeTestRule.onNodeWithText(DataProvider.songs[0].name).assertIsDisplayed()
        composeTestRule.onNodeWithText(DataProvider.songs[1].name).assertIsDisplayed()
    }

    @Test
    fun testSongClickTriggersEvents() {
        // Arrange: Set up the song click event
        var songClicked = false
        var backPressed = false
        composeTestRule.setContent {
            val mockState = AudioListUIState(songs = DataProvider.songs)
            AudioListScreen(modifier = Modifier,
                state = mockState,
                selectedMonk = DataProvider.monks.first(),
                playerUIState = PlayerUIState(),
                authUiState = AuthUiState(),
                onPlayerEvent = {},
                onEvent = {
                    when (it) {
                        is AudioListUIEvent.FetchSong -> {}
                        AudioListUIEvent.OnBackPress -> {
                            backPressed = true
                        }

                        is AudioListUIEvent.OnFavoriteIconClick -> {}
                        is AudioListUIEvent.OnSongClick -> {
                            songClicked = true
                        }
                    }
                },
                onSongClick = { songClicked = true },
                onSignIn = {

                })
        }

        // Assert: Verify that the song click event is triggered
        composeTestRule.onNodeWithText(DataProvider.songs[0].name).performClick()
        assert(songClicked)


        // Assert: Verify that the back press event is triggered
        composeTestRule.onNodeWithContentDescription(ContentDescriptions.BACK).performClick()
        assert(backPressed)
    }
}