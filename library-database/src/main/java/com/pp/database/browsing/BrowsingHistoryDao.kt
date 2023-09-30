package com.pp.database.browsing

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface BrowsingHistoryDao {

    /**
     * 添加一条数据,如果主键值一样就替换，如果不一样就添加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(browsingHistory: BrowsingHistory)

    @Delete
    fun remove(browsingHistory: BrowsingHistory)

    @Delete
    fun remove(list: List<BrowsingHistory>)

    @Query("SELECT * FROM BrowsingHistory WHERE userId =:userId ORDER BY latestTime DESC")
    fun pagingSource(userId: Long?): PagingSource<Int, BrowsingHistory>
}