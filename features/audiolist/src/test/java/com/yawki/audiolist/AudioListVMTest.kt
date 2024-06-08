package com.yawki.audiolist

import com.yawki.common.data.DataProvider
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.usecases.AddMediaItemsUseCase
import com.yawki.common.domain.usecases.GetSongsUseCase
import com.yawki.navigator.ComposeNavigator
import com.yawki.navigator.YawKiScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@OptIn(ExperimentalCoroutinesApi::class)
class AudioListVMTest {
    @Mock
    private lateinit var composeNavigator: ComposeNavigator

    @Mock
    private lateinit var getSongsUseCase: GetSongsUseCase

    @Mock
    private lateinit var addMediaItemsUseCase: AddMediaItemsUseCase

    private lateinit var audioListVM: AudioListVM
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        audioListVM = AudioListVM(composeNavigator, getSongsUseCase, addMediaItemsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAudios updates state to loading and then success`() = runTest {
        // Arrange
        val monkId = 1
        `when`(getSongsUseCase.invoke(monkId)).thenReturn(flow {
            emit(SafeResult.Loading)
            emit(SafeResult.Success(DataProvider.songs))
        })

        // Act
        audioListVM.onEvent(AudioListUIEvent.FetchSong(monkId))

        // Assert
        // Verify loading state
        assert(audioListVM.audioListScreenUIState.loading)

        // Advance the dispatcher to trigger the next emission
        advanceUntilIdle()

        // Verify success state
        assertFalse(audioListVM.audioListScreenUIState.loading)
        assertEquals(DataProvider.songs, audioListVM.audioListScreenUIState.songs)
        verify(addMediaItemsUseCase).invoke(DataProvider.songs)
    }

    @Test
    fun `fetchAudios updates state to loading and then error`() = runTest {
        // Arrange
        val monkId = 1
        val exception = Exception("Test Exception")
        `when`(getSongsUseCase.invoke(monkId)).thenReturn(flow {
            emit(SafeResult.Loading)
            emit(SafeResult.Error(exception))
        })

        // Act
        audioListVM.onEvent(AudioListUIEvent.FetchSong(monkId))

        // Assert
        // Verify loading state
        assert(audioListVM.audioListScreenUIState.loading)

        // Advance the dispatcher to trigger the next emission
        advanceUntilIdle()

        // Verify error state
        assertFalse(audioListVM.audioListScreenUIState.loading)
        assertTrue(audioListVM.audioListScreenUIState.error != null)
        assertEquals(exception, audioListVM.audioListScreenUIState.error)
    }

    @Test
    fun `onBackPress calls navigateUp on ComposeNavigator`() {
        // Arrange
        audioListVM.onEvent(AudioListUIEvent.OnBackPress)

        // Assert
        verify(composeNavigator).navigateUp()
    }

    @Test
    fun `onSongClick calls navigate to PlayerScreen on ComposeNavigator`() {
        // Arrange
        val song = DataProvider.songs[0]
        // Act
        audioListVM.onEvent(AudioListUIEvent.OnSongClick(song))
        // Assert
        verify(composeNavigator).navigate(YawKiScreens.PlayerUIScreen.route)
    }
}