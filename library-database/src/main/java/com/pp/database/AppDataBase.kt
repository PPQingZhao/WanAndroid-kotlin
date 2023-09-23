package com.pp.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pp.database.details.FeedDetails
import com.pp.database.user.User
import com.pp.database.user.UserDao
import kotlinx.coroutines.*

/**
 * 数据库
 */
@Database(entities = [User::class, FeedDetails::class], version = 1, exportSchema = true)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        private const val TAG = "AppDataBase"

        // 数据库名称
        private const val DB_NAME = "wanandroid.db"

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
            set(value) {
                if (field != null) {
                    throw RuntimeException("Do not initialize AppDataBase again")
                }

                field = value
            }
            get() = checkNotNull(field) { "you should call init(context) at first" }


        val instance: LiveData<AppDataBase> by lazy {
//            val v1_to_v2 = object : Migration(1, 2) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    // do nothing
//                }
//            }
//
//            val v2_to_v3 = object : Migration(2, 3) {
//                override fun migrate(database: SupportSQLiteDatabase) {
//                    // user表添加字段
//                    database.execSQL("alter table user add column token text")
//                    database.execSQL("alter table user add column e_mail text")
//                    database.execSQL("alter table user add column mobile text")
//                    database.execSQL("alter table user add column nickName text")
//                    database.execSQL("alter table user add column motto text")
//                    database.execSQL("alter table user add column avatar text")
//
//                }
//            }
//            Room.databaseBuilder(context!!, AppDataBase::class.java, DB_NAME)
//                .addMigrations(v1_to_v2)
//                .addMigrations(v2_to_v3)
//                .build()

            MutableLiveData<AppDataBase>().apply {
                postValue(
                    Room.databaseBuilder(context!!, AppDataBase::class.java, DB_NAME).build()
                )
            }
        }

        private fun init() {
            Log.v(TAG, "start init $DB_NAME")
            instance
            Log.v(TAG, "$DB_NAME init succeed")
        }

    }

    /**
     * 监听 app 生命周期,用于初始化数据库
     */
    internal class DatabaseInitializer private constructor() :
        DefaultLifecycleObserver {

        companion object {
            fun init(ctx: Context) {

                context = ctx
                ProcessLifecycleOwner.get().lifecycle.addObserver(DatabaseInitializer())
            }
        }

        override fun onCreate(owner: LifecycleOwner) {

            owner.lifecycleScope.launch(Dispatchers.IO) {
                // 数据库初始化
                init()
            }
            ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        }
    }

}



