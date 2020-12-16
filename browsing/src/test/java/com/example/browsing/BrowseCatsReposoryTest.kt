package com.example.browsing

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BrowseCatsRepositoryTest {
    private lateinit var repository: BrowseCatsRepository

    @Mock lateinit var apiService: BrowseService
    @Mock lateinit var dao: CatsDao

    private val mockedApiResponse = listOf(Cat("1", "url"))
    private val mockedCachedResponse = listOf(Cat("2", "url2"))
    @Before
    fun init() {
        repository = BrowseCatsRepository(apiService, dao)
        runBlocking {
        `when`(apiService.getCats(ArgumentMatchers.anyMap())).thenReturn(mockedApiResponse)
        `when`(dao.getAllRows()).thenReturn(mockedCachedResponse)
        }
    }

    @Test
    fun `should return cats from api`() {
        runBlocking {
            val params = mapOf<String, String>()
            val result = repository.fetchCatsFromApi(params)
            verify(apiService).getCats(params)
            assert(result?.equals(mockedApiResponse) ?: false)
        }
    }

    @Test
    fun `should return cats from db`() {
        runBlocking {
            val result = repository.getCachedEntries()
            verify(dao).getAllRows()
            assert(result?.equals(mockedCachedResponse) ?: false)
        }
    }

    @Test
    fun `should call dao to entries records`() {
        runBlocking {
            val entries = listOf(Cat("3", "url3"))
            repository.save(entries)
            verify(dao).insertRows(entries)
        }
    }
}