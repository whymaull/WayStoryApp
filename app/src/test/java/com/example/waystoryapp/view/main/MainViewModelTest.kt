package com.example.waystoryapp.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.waystoryapp.DataDummy
import com.example.waystoryapp.data.adapter.StoryListAdapter
import com.example.waystoryapp.data.database.Entities
import com.example.waystoryapp.data.reps.UserRepository
import com.example.waystoryapp.getOrAwaitValue
import com.example.waystoryapp.view.MainDispatcherRule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var quoteRepository: UserRepository
    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<Entities> = QuotePagingSource.snapshot(dummyQuote)
        val expectedQuote = MutableLiveData<PagingData<Entities>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getQuote()).thenReturn(expectedQuote)

        val mainViewModel = MainViewModel(quoteRepository)
        val actualQuote: PagingData<Entities> = mainViewModel.storyPage.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyQuote.size, differ.snapshot().size)
        Assert.assertEquals(dummyQuote[0], differ.snapshot()[0])
    }
    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<Entities> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<Entities>>()
        expectedQuote.value = data
        Mockito.`when`(quoteRepository.getQuote()).thenReturn(expectedQuote)
        val mainViewModel = MainViewModel(quoteRepository)
        val actualQuote: PagingData<Entities> = mainViewModel.storyPage.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)
        Assert.assertEquals(0, differ.snapshot().size)
    }
    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

class QuotePagingSource : PagingSource<Int, LiveData<List<Entities>>>() {
    companion object {
        fun snapshot(items: List<Entities>): PagingData<Entities> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Entities>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Entities>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}