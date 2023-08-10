package com.pp.database.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * user table
 */
@Entity(tableName = "user", indices = [Index(value = ["name"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo var name: String? = null,
    @ColumnInfo var password: String? = null,
    @ColumnInfo var token: String? = null,
    @ColumnInfo var e_mail: String? = null,
    @ColumnInfo var nickName: String? = null,
    @ColumnInfo var avatar: String? = null,
    @ColumnInfo var coinCount: Long? = null,
    @ColumnInfo var level: Long? = null,
    @ColumnInfo var rank: String? = null,
    @ColumnInfo var userId: Long? = null,
)