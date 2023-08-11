package com.pp.user.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemAllowRightViewModel

class UserItemAllowRightModel(
    @DrawableRes icon: Int,
    @StringRes content: Int,
    theme: AppDynamicTheme,
) : ItemAllowRightViewModel(icon, content, theme) {


}