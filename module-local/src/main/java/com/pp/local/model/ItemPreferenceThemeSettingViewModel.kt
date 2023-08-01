package com.pp.local.model

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pp.base.WanAndroidTheme
import com.pp.base.getPreferenceTheme
import com.pp.base.updateTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ItemPreferenceThemeSettingViewModel(
    @WanAndroidTheme.ThemeId themeId: Int,
    private val defaultTheme: Resources.Theme,
    private val displayMetrics: DisplayMetrics,
    private val configuration: Configuration,
) : ItemThemeSettingViewModel(themeId), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // 初始化
        WanAndroidTheme.getThemeById(
            themeId,
            defaultTheme,
            displayMetrics,
            configuration,
        ).run {
            appTheme.applyTheme(getTheme())
        }
        owner.lifecycleScope.launch {
            getPreferenceTheme().collectLatest {
                checked.set((it ?: WanAndroidTheme.Default) == themeId)
            }
        }
    }

    override fun onItemClick(v: View) {
        if (checked.get()) {
            return
        }
        ViewTreeLifecycleOwner.get(v)?.lifecycleScope?.launch {
            updateTheme(themeId)
        }
    }

}