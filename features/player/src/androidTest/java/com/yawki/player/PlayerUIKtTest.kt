package com.yawki.player

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.yawki.common.data.DataProvider
import com.yawki.common.presentation.PlayerUIState
import com.yawki.common.utils.ContentDescriptions
import com.yawki.common.utils.TestTags
import org.junit.Rule
import org.junit.Test

class PlayerUIKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingStateDisplayed() {
        // Arrange: Set up the loading state
        composeTestRule.setContent {
            val mockState = PlayerUIState(loading = true)
            PlayerScreen(
                uiState = mockState,
                monk = DataProvider.monks.first(),
                onEvent = {},
            )
        }

        // Assert: Verify that the loading indicator is displayed
        composeTestRule.onNodeWithTag(TestTags.LOADING_VIEW_TAG).assertIsDisplayed()
    }

    @Test
    fun testErrorStateDisplayed() {
        // Arrange: Set up the error state
        val errorText = "Test error"
        composeTestRule.setContent {
            val mockState = PlayerUIState(error = Exception(errorText))
            PlayerScreen(
                uiState = mockState,
                monk = DataProvider.monks.first(),
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithText(errorText).assertIsDisplayed()
    }

    @Test
    fun testPlayerTopAppBarDisplayed() {
        // Arrange: Set up the empty state
        val song = DataProvider.songs.first()
        composeTestRule.setContent {
            val mockState = PlayerUIState(currentSong = song)
            PlayerScreen(
                uiState = mockState,
                monk = null,
                onEvent = {}
            )
        }

        composeTestRule.onNodeWithTag(TestTags.PLAYER_TOP_APP_BAR).assertIsDisplayed()
    }

    @Test
    fun testCenterImageDisplayed() {
        val song = DataProvider.songs.first()

        composeTestRule.setContent {
            PlayerScreen(uiState = PlayerUIState(currentSong = song), monk = null) {
            }
        }

        composeTestRule.onNodeWithTag(TestTags.PLAYER_IMAGE).assertIsDisplayed()
    }

    @Test
    fun testPlayerFileNameDisplaysCorrectInformation() {
        val song = DataProvider.songs.first()
        val monk = DataProvider.monks.first()
        composeTestRule.setContent {
            PlayerScreen(uiState = PlayerUIState(currentSong = song), monk = monk) {
            }
        }

        composeTestRule.onNodeWithTag(TestTags.PLAYER_FILE_NAME)
            .assertTextContains(song.name)
        composeTestRule.onNodeWithTag(TestTags.PLAYER_MONK_NAME)
            .assertTextContains(monk.name)
    }

    @Test
    fun testPlayerPlayPauseButtonTriggersCorrectEvents() {
        var eventTriggered = false
        val song = DataProvider.songs.first()

        composeTestRule.setContent {
            PlayerScreen(
                uiState = PlayerUIState(
                    currentSong = song
                ),
                monk = null,
                onEvent = {
                    if (it is PlayerUIEvent.ResumeSong || it is PlayerUIEvent.PauseSong) {
                        eventTriggered = true
                    }
                }
            )
        }

        // Find and click the play/pause button
        composeTestRule.onNodeWithContentDescription(ContentDescriptions.PLAYER_PLAY_PAUSE_BUTTON)
            .performClick()

        assert(eventTriggered)
    }

    @Test
    fun testSkipToNextSongButtonTriggersCorrectEvents() {
        var eventTriggered = false
        val song = DataProvider.songs.first()
        composeTestRule.setContent {
            PlayerScreen(
                uiState = PlayerUIState(
                    currentSong = song
                ),
                monk = null,
                onEvent = {
                    if (it is PlayerUIEvent.SkipToNextSong) {
                        eventTriggered = true
                    }
                }
            )
        }

        composeTestRule.onNodeWithContentDescription(ContentDescriptions.SKIP_TO_NEXT_SONG)
            .performClick()

        assert(eventTriggered)
    }

    @Test
    fun testSkipToPreviousSongButtonTriggersCorrectEvents() {
        var eventTriggered = false
        val song = DataProvider.songs.first()
        composeTestRule.setContent {
            PlayerScreen(
                uiState = PlayerUIState(
                    currentSong = song
                ),
                monk = null,
                onEvent = {
                    if (it is PlayerUIEvent.SkipToPreviousSong) {
                        eventTriggered = true
                    }
                }
            )
        }

        composeTestRule.onNodeWithContentDescription(ContentDescriptions.SKIP_TO_PREVIOUS_SONG)
            .performClick()

        assert(eventTriggered)
    }

    @Test
    fun testSkipAndRewind10SecondsButtonTriggersCorrectEvents() {
        var triggerSkip10Seconds = false
        var triggerReplay10Seconds = false
        val song = DataProvider.songs.first()
        composeTestRule.setContent {
            PlayerScreen(
                uiState = PlayerUIState(
                    currentSong = song
                ),
                monk = null,
                onEvent = {
                    if (it is PlayerUIEvent.SeekSongToPosition) {
                        triggerSkip10Seconds = true
                        triggerReplay10Seconds = true
                    }
                }
            )
        }

        composeTestRule.onNodeWithContentDescription(ContentDescriptions.SKIP_10_SECONDS)
            .performClick()
        composeTestRule.onNodeWithContentDescription(ContentDescriptions.REPLAY_10_SECONDS)
            .performClick()
        assert(triggerSkip10Seconds)
        assert(triggerReplay10Seconds)
    }
}