package com.example.browsing

class BrowseCatsRepository(
    private val apiService: BrowseService,
    private val catsDao: CatsDao
){

    suspend fun fetchCatsFromApi(map: Map<String, String>): List<Cat>? {
        return apiService.getCats(map)
    }

    suspend fun save(entries: List<Cat>) {
        catsDao.insertRows(entries)
    }

    suspend fun hasCachedEntries(): Int {
        return catsDao.getRowsCount()
    }

    suspend fun getCachedEntries(limit: Int, offset: Int): List<Cat>? {
        return catsDao.getRowsWith(limit, offset)
    }

    suspend fun getAll(): List<Cat>? {
        return catsDao.getAllRows()
    }
}