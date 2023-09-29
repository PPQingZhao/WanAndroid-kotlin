package com.pp.database.browsing

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 浏览记录
 */
@Entity(tableName = "BrowsingHistory")
class BrowsingHistory(
    @PrimaryKey(autoGenerate = false) var userId: Long = 0,
    @ColumnInfo var latestTime: Long? = null,
    @ColumnInfo var latestTimeText: String? = null,
    @ColumnInfo var url: String? = null,
    @ColumnInfo var title: String? = null,
)