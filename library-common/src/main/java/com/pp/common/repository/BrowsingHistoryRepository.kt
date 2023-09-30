package com.pp.common.repository

import android.annotation.SuppressLint
import androidx.paging.*
import com.pp.database.AppDataBase
import com.pp.database.browsing.BrowsingHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

object BrowsingHistoryRepository {

    private val browsingHistoryDao by lazy { AppDataBase.instance.value!!.getBrowsingHistoryDao() }

    @SuppressLint("SimpleDateFormat")
    suspend fun addHistoryWithTime(userId: Long, title: String?, url: String) {
        withContext(Dispatchers.IO) {
            val date = Date()
            val time = date.time
            val timeText = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date)
            browsingHistoryDao.add(
                BrowsingHistory(
                    userId = userId,
                    title = title,
                    url = url,
                    latestTime = time,
                    latestTimeText = timeText
                )
            )
        }
    }

    suspend fun remove(browsingHistory: BrowsingHistory) {
        withContext(Dispatchers.IO) {
            browsingHistoryDao.remove(browsingHistory)
        }
    }

    suspend fun remove(list: List<BrowsingHistory>) {
        withContext(Dispatchers.IO) {
            browsingHistoryDao.remove(list)
        }
    }

    fun getPageData(userId: Long?): Flow<PagingData<BrowsingHistory>> {
        return Pager(
            initialKey = 1,
            config = PagingConfig(15),
            pagingSourceFactory = { browsingHistoryDao.pagingSource(userId) }).flow
    }


}