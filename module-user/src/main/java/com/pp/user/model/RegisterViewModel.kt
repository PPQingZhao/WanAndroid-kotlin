package com.pp.user.model

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData

open class RegisterViewModel {
    val registerEnable = ObservableBoolean(false)
    val errorMessage = MutableLiveData<String>("")
    val helperMessage = MutableLiveData<String>("")
    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val confirmPassword = MutableLiveData<String>("")
    val succeed = MutableLiveData<Boolean>()

    open fun onClick(view: View) {}

    open fun onReturn(view: View) {}
}