package com.pp.database.browsing

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BrowsingHistoryDao {

    /**
     * 添加一条数据,如果主键值一样就替换，如果不一样就添加
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(browsingHistory: BrowsingHistory)

    @Delete
    fun remove(browsingHistory: BrowsingHistory)

    @Query("SELECT * FROM BrowsingHistory")
    fun getBrowsingAll(): LiveData<List<BrowsingHistory>>
}