package com.pp.home.repository

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pp.common.app.App
import com.pp.common.constant.KEY_SAVE_SEARCH_HOTKEY_HISTORY
import com.pp.common.constant.MAX_COUNT_SEARCH_HOTKEY_HISTORY
import com.pp.common.constant.preferences_key_search_hotkey_history
import com.pp.common.datastore.userDataStore
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKey
import com.pp.common.http.wanandroid.bean.PageBean
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.paging.WanPagingSource
import kotlinx.coroutines.flow.*

object SearchRepository {

    private const val DEBUG = false

    suspend fun clearSearchHistory() {
        App.getInstance().userDataStore.edit {
            it.remove(preferences_key_search_hotkey_history)
        }
    }

    /**
     * 获取搜索历史记录
     */
    fun getSearchHotkeyHistory(): Flow<List<HotKey>> {
        return channelFlow<List<HotKey>> {
            App.getInstance().userDataStore.data.collectLatest {
                val history = it[preferences_key_search_hotkey_history]
                if (DEBUG) {
                    Log.e("TAG", "get search history: $history")
                }

                mutableListOf<HotKey>().apply {
                    history?.split(KEY_SAVE_SEARCH_HOTKEY_HISTORY)
                        ?.filter {
                            it.isNotBlank()
                        }
                        ?.forEachIndexed { index, item ->
                            add(HotKey(id = index, name = item))
                        }

                }.apply {
                    send(this)
                }
            }
        }
    }

    /**
     * 保存搜索记录
     */
    suspend fun saveSearchHotKeyHistory(history: String) {
        if (history.isBlank()) {
            return
        }
        App.getInstance().userDataStore.edit {
            it[preferences_key_search_hotkey_history].let { oldHistory ->
                var oldItems = ""
                oldHistory?.split(KEY_SAVE_SEARCH_HOTKEY_HISTORY)
                    ?.filter { item ->
                        item.isNotBlank() && item != history
                    }
                    // 根据缓存数量最大值过滤
                    ?.filterIndexed { index, item ->
                        index < MAX_COUNT_SEARCH_HOTKEY_HISTORY - 1
                    }?.onEach { item ->
                        oldItems += "$item;"
                    }

                //更新搜索记录
                it[preferences_key_search_hotkey_history] =
                    (if (oldItems.isNotBlank()) "$history$KEY_SAVE_SEARCH_HOTKEY_HISTORY$oldItems" else history).also { history ->
                        if (DEBUG) {
                            Log.e("TAG", "save search history: $history")
                        }
                    }
            }

        }
    }

    suspend fun getHotKey(): ResponseBean<List<HotKey>> {
        return WanAndroidService.searchApi.getHotkey()
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(15),
            pagingSourceFactory = { SearchPageSources(key) }).flow
    }

    suspend fun removeSearchHistory(history: String) {
        App.getInstance().userDataStore.edit {
            it[preferences_key_search_hotkey_history].let { oldHistory ->
                var oldItems = ""
                oldHistory?.split(KEY_SAVE_SEARCH_HOTKEY_HISTORY)
                    ?.filter { item ->
                        item.isNotBlank() && item != history
                    }?.onEach { item ->
                        oldItems += "$item;"
                    }

                //更新搜索记录
                it[preferences_key_search_hotkey_history] = oldItems.also { item ->
                    if (DEBUG) {
                        Log.e("TAG", "remove search history: $history  $item")
                    }
                }
            }
        }
    }

    private class SearchPageSources(private val key: String?) : WanPagingSource() {

        override suspend fun getPageData(page: Int): PageBean? {
            return WanAndroidService.searchApi.queryArticle(page, key).data
        }

        override fun createNextKey(response: PageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }
}