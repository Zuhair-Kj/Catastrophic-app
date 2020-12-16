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

    suspend fun getCachedEntries(): List<Cat>? {
        return catsDao.getAllRows()
    }

//    suspend fun getCachedEntries(limit: Int, offset: Int): List<Cat>? {
//        return catsDao.getRowsWith(limit, offset)
//    }
}