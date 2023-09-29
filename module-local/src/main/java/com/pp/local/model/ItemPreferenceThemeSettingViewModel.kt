package com.pp.local.model

import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pp.theme.AppDynamicTheme
import com.pp.theme.DynamicThemeManager
import com.pp.theme.applySkinTheme
import com.pp.theme.listenerSkinTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ItemPreferenceThemeSettingViewModel(
    private val skinTheme: DynamicThemeManager.ApplySkinTheme,
    displayMetrics: DisplayMetrics,
    configuration: Configuration,
) : ItemThemeSettingViewModel<Any>(AppDynamicTheme(displayMetrics, configuration, "Theme.Dynamic")),
    DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        themeName.set(skinTheme.name)
        theme.applySkinTheme(skinTheme)

        //监听主题变化
        owner.lifecycleScope.launch {
            listenerSkinTheme().collectLatest {
                isSelected.set(it == skinTheme)
            }
        }
    }

    override fun onItemViewModelClick(view: View): Boolean {
        if (isSelected.get()) {
            return true
        }

        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch {
            DynamicThemeManager.applySkinTheme(skinTheme)
        }
        return super.onItemViewModelClick(view)
    }

}