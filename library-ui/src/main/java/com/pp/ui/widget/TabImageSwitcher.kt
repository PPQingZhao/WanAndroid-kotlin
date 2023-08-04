package com.pp.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.pp.ui.R


@SuppressLint("ViewConstructor")
class TabImageSwitcher : ImageSwitcher {

    constructor(
        context: Context,
        @DrawableRes unSelectedIcon: Int,
        @DrawableRes selectedIcon: Int,
    ) : super(context) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setFactory {
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            layoutParams.gravity = Gravity.CENTER
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                this.layoutParams = layoutParams
                scaleX = 0.8f
                scaleY = 0.8f
            }
        }
        setImageResource(selectedIcon)
        setImageResource(unSelectedIcon)
    }

    @SuppressLint("ResourceType")
    override fun setSelected(selected: Boolean) {
        if (isSelected == selected) {
            // do not thing
            return
        }
        if (selected) {
            setInAnimation(context, R.anim.anim_tab_selected_in)
            setOutAnimation(context, R.anim.anim_tab_unselected_out)
            showNext()
        } else {
            setInAnimation(context, R.anim.anim_tab_unselected_in)
            setOutAnimation(context, R.anim.anim_tab_selected_out)
            showPrevious()
        }
        super.setSelected(selected)
    }
}