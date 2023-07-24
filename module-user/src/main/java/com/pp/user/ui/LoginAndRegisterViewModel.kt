package com.pp.user.ui

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleOwner
import com.pp.base.ThemeViewModel
import com.pp.user.R
import com.pp.user.model.UserLoginViewModel
import com.pp.user.model.UserRegisterViewModel

class LoginAndRegisterViewModel(application: Application) : ThemeViewModel(application) {
    val title = ObservableField<Int>(R.string.user_login)
    val loginViewModel = UserLoginViewModel()
    val registerViewModel = UserRegisterViewModel()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        owner.lifecycle.addObserver(loginViewModel)
        owner.lifecycle.addObserver(registerViewModel)
    }
}