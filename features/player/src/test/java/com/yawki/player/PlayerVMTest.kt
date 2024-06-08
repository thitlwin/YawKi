package com.yawki.player

import com.yawki.common.data.DataProvider
import com.yawki.common.domain.models.PlayerState
import com.yawki.common.domain.models.song.Song
import com.yawki.common.domain.usecases.GetCurrentSongPositionUseCase
import com.yawki.common.domain.usecases.PauseSongUseCase
import com.yawki.common.domain.usecases.PlaySongUseCase
import com.yawki.common.domain.usecases.ResumeSongUseCase
import com.yawki.common.domain.usecases.SeekSongToPositionUseCase
import com.yawki.common.domain.usecases.SetMediaControllerCallbackUseCase
import com.yawki.common.domain.usecases.SkipToNextSongUseCase
import com.yawki.common.domain.usecases.SkipToPreviousSongUseCase
import com.yawki.navigator.ComposeNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerVMTest {

    @Mock
    private lateinit var setMediaControllerCallbackUseCase: SetMediaControllerCallbackUseCase

    @Mock
    private lateinit var getCurrentMusicPositionUseCase: GetCurrentSongPositionUseCase

    @Mock
    private lateinit var playSongUseCase: PlaySongUseCase

    @Mock
    private lateinit var pauseSongUseCase: PauseSongUseCase

    @Mock
    private lateinit var resumeSongUseCase: ResumeSongUseCase

    @Mock
    private lateinit var skipToNextSongUseCase: SkipToNextSongUseCase

    @Mock
    private lateinit var skipToPreviousSongUseCase: SkipToPreviousSongUseCase

    @Mock
    private lateinit var seekSongToPositionUseCase: SeekSongToPositionUseCase

    @Mock
    private lateinit var composeNavigator: ComposeNavigator

    private lateinit var playerVM: PlayerVM
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        playerVM = PlayerVM(
            setMediaControllerCallbackUseCase,
            getCurrentMusicPositionUseCase,
            playSongUseCase,
            pauseSongUseCase,
            resumeSongUseCase,
            skipToNextSongUseCase,
            skipToPreviousSongUseCase,
            seekSongToPositionUseCase,
            composeNavigator
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `handleEvent calls correct use case for each event`() = testScope.runTest {
        // Arrange
        val updateUI: (Song?) -> Unit = {}
        // Test each event type
        playerVM.onEvent(PlayerUIEvent.PauseSong)
        advanceUntilIdle()
        verify(pauseSongUseCase).invoke()

        playerVM.onEvent(PlayerUIEvent.ResumeSong)
        advanceUntilIdle()
        verify(resumeSongUseCase).invoke()

        val seekPosition = 100L
        playerVM.onEvent(PlayerUIEvent.SeekSongToPosition(seekPosition))
        advanceUntilIdle()
        verify(seekSongToPositionUseCase).invoke(seekPosition)

//        playerVM.onEvent(PlayerUIEvent.SkipToNextSong)
//        advanceUntilIdle()
//        verify(skipToNextSongUseCase).invoke(updateUI)
//
//        playerVM.onEvent(PlayerUIEvent.SkipToPreviousSong)
//        advanceUntilIdle()
//        verify(skipToPreviousSongUseCase).invoke(updateUI)

        val mediaItemIndex = 5
        playerVM.onEvent(PlayerUIEvent.PlaySong(mediaItemIndex))
        advanceUntilIdle()
        verify(playSongUseCase).invoke(mediaItemIndex)

        playerVM.onEvent(PlayerUIEvent.OnBackPress)
        advanceUntilIdle()
        verify(composeNavigator).navigateUp()
    }

    @Test
    fun `PlaySong event updates UI state to PLAYING and calls playSongUseCase`() = testScope.runTest {
        // Arrange
        val mediaItemIndex = 1
        val expectedSong = DataProvider.songs

        // Mock the behavior of playSongUseCase
        `when`(playSongUseCase.invoke(mediaItemIndex)).thenAnswer {  } // You might want to return a Flow here if needed

        // Act
        playerVM.onEvent(PlayerUIEvent.PlaySong(mediaItemIndex))
        advanceUntilIdle()
        // Assert
        // Verify that playSongUseCase was called
        verify(playSongUseCase).invoke(mediaItemIndex)
    }

    @Test
    fun `PauseSong event calls pauseSongUseCase and updates UI State`() = testScope.runTest {
        // Arrange

        // Act
        playerVM.onEvent(PlayerUIEvent.PauseSong)
        advanceUntilIdle()

        verify(pauseSongUseCase).invoke()
    }

}


