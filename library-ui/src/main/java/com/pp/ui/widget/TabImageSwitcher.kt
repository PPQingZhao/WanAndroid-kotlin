package com.pp.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.get
import com.pp.ui.R


@SuppressLint("ViewConstructor")
open class TabImageSwitcher : ImageSwitcher {

    protected val mUnSelectedImageView: ImageView
    protected val mSelectedImageView: ImageView

    constructor(context: Context) : super(context) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        this.setFactory {
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            layoutParams.gravity = Gravity.CENTER
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                this.layoutParams = layoutParams
                scaleX = 0.8f
                scaleY = 0.8f
            }
        }

        mUnSelectedImageView = (get(0) as ImageView)
        mSelectedImageView = (get(1) as ImageView)
    }

    @SuppressLint("ResourceType")
    override fun setSelected(selected: Boolean) {
        if (isSelected == selected) {
            // do not thing
            return
        }
        displayedChild = if (selected) {
            setInAnimation(context, R.anim.anim_tab_selected_in)
            setOutAnimation(context, R.anim.anim_tab_unselected_out)
            1
        } else {
            setInAnimation(context, R.anim.anim_tab_unselected_in)
            setOutAnimation(context, R.anim.anim_tab_selected_out)
            0
        }
        super.setSelected(selected)
    }
}