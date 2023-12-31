package com.pp.main.widget

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.pp.theme.DynamicTheme
import com.pp.theme.getColor
import com.pp.ui.widget.TabImageSwitcher

@SuppressLint("ViewConstructor")
class WanAndroidTabImageSwitcher(
    context: Context,
    private val theme: DynamicTheme,
    private val selectedIconName: String,
    private val unSelectedIconName: String,
) : TabImageSwitcher(context) {

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
    private val themeInfoObserver = Observer<DynamicTheme.Info> {


        it?.theme?.resources?.run {

            it.theme.getColor(R.attr.navigationBarColor, Color.TRANSPARENT)
                .run {
                    mSelectedImageView.setBackgroundColor(this)
                    mUnSelectedImageView.setBackgroundColor(this)
                }

            getIdentifier(selectedIconName, "drawable", it.themePackage).run {
//                Log.e("TAG", "selectedIconName: $selectedIconName")
                getDrawable(this, it.theme).run { mSelectedImageView.setImageDrawable(this) }
            }

            getIdentifier(unSelectedIconName, "drawable", it.themePackage).run {
//                Log.e("TAG", "unSelectedIconName: $unSelectedIconName")
                getDrawable(this, it.theme).run { mUnSelectedImageView.setImageDrawable(this) }
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ViewTreeLifecycleOwner.get(this)?.run {
            theme.themeInfo.observe(this, themeInfoObserver)
        }
    }
}