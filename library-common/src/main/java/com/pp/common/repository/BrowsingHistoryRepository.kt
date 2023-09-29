package com.pp.common.repository

import androidx.lifecycle.LiveData
import com.pp.database.AppDataBase
import com.pp.database.browsing.BrowsingHistory

object BrowsingHistoryRepository {

    private val browsingHistoryDao by lazy { AppDataBase.instance.value!!.getBrowsingHistoryDao() }

    fun getBrowsingHistoryAll(): LiveData<List<BrowsingHistory>> {
        return browsingHistoryDao.getBrowsingAll()
    }

    fun add(browsingHistory: BrowsingHistory) {
        browsingHistoryDao.add(browsingHistory)
    }

    fun remove(browsingHistory: BrowsingHistory) {
        browsingHistoryDao.remove(browsingHistory)
    }
}