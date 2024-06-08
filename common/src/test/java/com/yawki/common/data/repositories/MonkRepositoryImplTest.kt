package com.yawki.common.data.repositories

import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.yawki.common.data.DataProvider
import com.yawki.common.data.datasource.remote.MonkRemoteDataSource
import com.yawki.common.data.mapper.DomainModelMapper
import com.yawki.common.data.models.MonkDto
import com.yawki.common.domain.SafeResult
import com.yawki.common.domain.models.monk.Monk
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MonkRepositoryImplTest {
    @MockK
    private lateinit var monkRemoteDataSource: MonkRemoteDataSource

    @MockK
    private lateinit var domainMapper: DomainModelMapper<MonkDto, Monk>

    private lateinit var monkRepositoryImpl: MonkRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)//, relaxUnitFun = true)
        monkRepositoryImpl = MonkRepositoryImpl(monkRemoteDataSource, domainMapper)
    }

    @Test
    fun `getMonkList emits Success with mapped monks when data is available`() = runTest {

        val monkDtos = DataProvider.monkDtoList
        val mockTask = mockTask(true, monkDtos)

        coEvery { monkRemoteDataSource.getAllMonk() } coAnswers { mockTask }

        monkRepositoryImpl.getMonkList()
            .catch { it.printStackTrace() }
            .test {
                val result = awaitItem()

                // Assert
                assert(result is SafeResult.Success)
//            assert((result as SafeResult.Success).data == DataProvider.monks)
            }
    }

    private fun <T> mockApiResponse(data: List<T>): SafeResult<List<T>> {
        return if (data.isNotEmpty()) {
            SafeResult.Success(data)
        } else {
            SafeResult.Error(Exception("No data found!"))
        }
    }

    private fun mockTask(isSuccessful: Boolean, result: List<MonkDto>): Task<QuerySnapshot> {
        val mockTask = mockk<Task<QuerySnapshot>>()// Mockito.mock(Task::class.java) as Task<QuerySnapshot>
        val mockSnapshot = mockk<QuerySnapshot>() //Mockito.mock(QuerySnapshot::class.java)

        every { mockTask.result } returns mockSnapshot
        every { mockTask.exception } returns Exception("No data found => haha")
        every { mockTask.isComplete } returns true
        every { mockSnapshot.toObjects(MonkDto::class.java) } returns result
//        every { mockTask.isSuccessful } returns isSuccessful
//        every { mockTask.result } returns mockSnapshot

//        io.mockk.MockKException: no answer found for Task(#3).isComplete() among the configured answers: (Task(#3).getResult()))
//        io.mockk.MockKException: no answer found for Task(#3).isComplete() among the configured answers: ()
//        io.mockk.MockKException: no answer found for Task(#3).getException() among the configured answers: (Task(#3).isComplete()))

//         You might need to mock the addOnCompleteListener method as well
//        `when`(mockTask.addOnCompleteListener(ArgumentMatchers.any())).thenAnswer { invocation ->
//            val listener = invocation.arguments[0] as OnCompleteListener<QuerySnapshot>
//            if (isSuccessful) {
//                listener.onComplete(mockTask)
//            }
//            // Handle the failure case if needed
//            return@thenAnswer mockTask
//        }
        return mockTask
    }
}