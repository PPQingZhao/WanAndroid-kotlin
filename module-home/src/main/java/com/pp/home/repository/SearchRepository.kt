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
import com.pp.common.http.wanandroid.bean.*
import com.pp.common.paging.ArticlePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

object SearchRepository {

    private const val DEBUG = false

    suspend fun clearSearchHistory(userId: Long?) {
        App.getInstance().userDataStore.edit {
            it.remove(preferences_key_search_hotkey_history(userId))
        }
    }

    /**
     * 获取搜索历史记录
     */
    fun getSearchHotkeyHistory(userId: Long?): Flow<List<HotKeyBean>> {
        return channelFlow<List<HotKeyBean>> {
            App.getInstance().userDataStore.data.collectLatest {
                val history = it[preferences_key_search_hotkey_history(userId)]
                if (DEBUG) {
                    Log.e("TAG", "get search history: $history")
                }

                mutableListOf<HotKeyBean>().apply {
                    history?.split(KEY_SAVE_SEARCH_HOTKEY_HISTORY)
                        ?.filter {
                            it.isNotBlank()
                        }
                        ?.forEachIndexed { index, item ->
                            add(HotKeyBean(id = index, name = item))
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
    suspend fun saveSearchHotKeyHistory(userId: Long?, history: String) {
        if (history.isBlank()) {
            return
        }
        App.getInstance().userDataStore.edit {
            it[preferences_key_search_hotkey_history(userId)].let { oldHistory ->
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
                it[preferences_key_search_hotkey_history(userId)] =
                    (if (oldItems.isNotBlank()) "$history$KEY_SAVE_SEARCH_HOTKEY_HISTORY$oldItems" else history).also { history ->
                        if (DEBUG) {
                            Log.e("TAG", "save search history: $history")
                        }
                    }
            }

        }
    }

    suspend fun getHotKey(): ResponseBean<List<HotKeyBean>> {
        return runCatchingResponse {
            WanAndroidService.searchApi.getHotkey()
        }
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return Pager(
            initialKey = 0,
            config = PagingConfig(15),
            pagingSourceFactory = { SearchPageSources(key) }).flow
    }

    suspend fun removeSearchHistory(userId: Long?, history: String) {
        App.getInstance().userDataStore.edit {
            it[preferences_key_search_hotkey_history(userId)].let { oldHistory ->
                var oldItems = ""
                oldHistory?.split(KEY_SAVE_SEARCH_HOTKEY_HISTORY)
                    ?.filter { item ->
                        item.isNotBlank() && item != history
                    }?.onEach { item ->
                        oldItems += "$item;"
                    }

                //更新搜索记录
                it[preferences_key_search_hotkey_history(userId)] = oldItems.also { item ->
                    if (DEBUG) {
                        Log.e("TAG", "remove search history: $history  $item")
                    }
                }
            }
        }
    }

    private class SearchPageSources(private val key: String?) : ArticlePagingSource() {

        override suspend fun getPageData(page: Int): ArticlePageBean? {
            return WanAndroidService.searchApi.queryArticle(page, key).data
        }

        override fun createNextKey(response: ArticlePageBean?): Int? {
            return super.createNextKey(response)?.plus(1)
        }

    }
}