package com.pp.home.ui

import android.app.Application
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.pp.base.ThemeViewModel
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.http.wanandroid.bean.HotKeyBean
import com.pp.common.model.ItemTextDeleteHotkeyViewModel
import com.pp.common.paging.*
import com.pp.common.repository.UserRepository
import com.pp.database.user.User
import com.pp.home.repository.SearchRepository
import com.pp.ui.R
import com.pp.ui.adapter.BindingPagingDataAdapter
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : ThemeViewModel(app) {

    private val isDeleteModel = MutableStateFlow(false)

    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText

    val mHistoryAdapter by lazy {
        val onItemListener = object : OnItemListener<ItemDataViewModel<HotKeyBean>> {
            override fun onItemClick(view: View, item: ItemDataViewModel<HotKeyBean>): Boolean {
                when (view.id) {
                    R.id.tv_delete_all -> {
                        clearSearchHistory()
                        isDeleteModel.value = false
                    }
                    R.id.tv_finish -> {
                        isDeleteModel.value = false
                    }
                    R.id.iv_delete_model -> {
                        isDeleteModel.value = true
                    }
                    R.id.container_text_delete -> {
                        (item as ItemTextDeleteHotkeyViewModel).let {
                            it.text.get()?.let { text ->
                                if (isDeleteModel.value) {
                                    removeSearchHistory(text);
                                } else {
                                    _searchText.value = text
                                }
                            }
                        }
                    }
                }
                return false
            }
        }

        BindingPagingDataAdapter<HotKeyBean>(
            {
                if (it is HotKeyBean && it.id >= 0) {
                    R.layout.item_text_delete
                } else {
                    R.layout.item_delete_bar
                }
            },
            diffCallback = hotKeyDifferCallback
        ).apply {
            itemTextDeleteHotkeyBinder(
                onCreateViewModel = { model ->
                    viewModelScope.launch {
                        isDeleteModel.collectLatest {
                            model.isDeleteModel.value = it
                        }
                    }
                },
                onItemListener = onItemListener,
                theme = mTheme
            ).also {
                addItemViewModelBinder(it)
            }

            itemDeleteBarHotkeyBinder(
                onItemListener = onItemListener,
                theme = mTheme
            ).also {
                addItemViewModelBinder(it)
            }
        }
    }

    val mHotkeyAdapter by lazy {
        val onItemListener = object : OnItemListener<ItemDataViewModel<HotKeyBean>> {
            override fun onItemClick(view: View, item: ItemDataViewModel<HotKeyBean>): Boolean {
                item.data?.name.let {
                    _searchText.value = it
                }
                return true
            }
        }

        BindingPagingDataAdapter<HotKeyBean>(
            {
                if (it is HotKeyBean && it.id >= 0) {
                    com.pp.ui.R.layout.item_text2
                } else {
                    com.pp.ui.R.layout.item_text1
                }
            },
            diffCallback = hotKeyDifferCallback
        ).apply {
            itemText1HotkeyBinder(mTheme).also {
                addItemViewModelBinder(it)
            }

            itemText2HotkeyBinder(onItemListener, mTheme).also {
                addItemViewModelBinder(it)
            }
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO) {
            async {
                UserRepository.preferenceUser().collectLatest {
                    updateSearchHistory(it)
                }
            }

            async {
                getSearchHot().collectLatest {
                    mHotkeyAdapter.setPagingData(this, it)
                }
            }
        }
    }

    private fun updateSearchHistory(user: User?) {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchHotkeyHistory(user?.userId).collectLatest {
                mHistoryAdapter.setData(this, it)
            }
        }
    }

    // 热门搜索
    private fun getSearchHot(): Flow<PagingData<HotKeyBean>> {
        return onePager(getPageData = { SearchRepository.getHotKey().data },
            getPageValue = {
                it ?: emptyList()
            }).flow.map {
            it.insertSeparators { before, after ->
                if (before == null && after != null) {
                    HotKeyBean(
                        name = getApplication<Application>().resources.getString(R.string.hot_key)
                    )
                } else {
                    null
                }
            }
        }.cachedIn(viewModelScope)
    }

    fun searchPageData(key: String?): Flow<PagingData<ArticleBean>> {
        return SearchRepository.searchPageData(key).cachedIn(viewModelScope)
    }

    /**
     * 获取搜索记录
     */
    private fun getSearchHotkeyHistory(userId: Long?): Flow<List<HotKeyBean>> {
        return SearchRepository.getSearchHotkeyHistory(userId).map {
            mutableListOf<HotKeyBean>().apply {
                if (it.isEmpty()) {
                    return@apply
                }
                add(HotKeyBean(name = getApplication<Application>().resources.getString(R.string.search_history)))
                addAll(it)
            }
        }
    }

    /**
     * 保存搜索记录
     */
    fun saveSearchHotKeyHistory(history: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.saveSearchHotKeyHistory(
                UserRepository.getPreferenceUser()?.userId,
                history
            )
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.clearSearchHistory(UserRepository.getPreferenceUser()?.userId)
        }
    }

    fun removeSearchHistory(history: String) {
        viewModelScope.launch(Dispatchers.IO) {
            SearchRepository.removeSearchHistory(
                UserRepository.getPreferenceUser()?.userId,
                history
            )
        }
    }

}