package com.pp.ui.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.pp.theme.AppDynamicTheme

open class ItemArticleViewModel(val theme: AppDynamicTheme) {
    val title = ObservableField<String>()
    val author = ObservableField<String>()
    val chapterName = ObservableField<String>()
    val niceDate = ObservableField<String>()
    val tags = ObservableField<String>()
    val transitionName = ObservableField<String>()

    // 置顶
    val isPinned = ObservableBoolean(false)

    // 新发布
    val isFresh = ObservableBoolean(false)

    // 收藏
    val isCollect = ObservableBoolean(false)

    open fun onItemClick(v: View) {

    }

    open fun onCollect(v: View) {

    }

}