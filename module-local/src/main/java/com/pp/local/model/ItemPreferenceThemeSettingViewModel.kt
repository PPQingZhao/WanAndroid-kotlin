package com.pp.local.model

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
    theme: AppDynamicTheme,
) : ItemThemeSettingViewModel<Any>(theme), DefaultLifecycleObserver {

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
            skinTheme.applySkinTheme()
        }
        return super.onItemViewModelClick(view)
    }

}