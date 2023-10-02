package com.pp.ui.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pp.theme.AppDynamicTheme
import com.pp.theme.DynamicThemeManager
import com.pp.theme.applySkinTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThemeFloatingView : ImageFilterView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setOnClickListener {
            applyNextTheme()
        }
    }

    private var job: Job? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val appDynamicTheme = AppDynamicTheme(
            DisplayMetrics().apply {
                setTo(context.theme.resources.displayMetrics)
            },
            Configuration().apply {
                setTo(context.theme.resources.configuration)
            }, "Theme.Dynamic"
        )

        job = ViewTreeLifecycleOwner.get(this)?.lifecycleScope?.launch {
            DynamicThemeManager.getCurrentApplySkinTheme().collectLatest {
                val nextSkinTheme = DynamicThemeManager.nextSkinTheme(it)
                appDynamicTheme.applySkinTheme(nextSkinTheme)
                setBackgroundColor(appDynamicTheme.colorPrimary.value!!.defaultColor)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job?.cancel()
    }


    private fun applyNextTheme() {
        ViewTreeLifecycleOwner.get(this)?.lifecycleScope?.launch(Dispatchers.IO) {
            DynamicThemeManager.applyNextSkinTheme()
        }
    }


}