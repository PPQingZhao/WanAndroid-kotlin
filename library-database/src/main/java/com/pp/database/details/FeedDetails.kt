package com.pp.database.details

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
class FeedDetails(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo var resourceId: String? = null,
    @ColumnInfo var resourceType: String? = null,
    @ColumnInfo var text: String? = null,
    @ColumnInfo var cacheUrl: String? = null,
    @ColumnInfo var duration: Long = 0,
)