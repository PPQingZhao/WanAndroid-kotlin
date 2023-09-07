package com.pp.mvvm

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

open class LifecycleViewModel(app: Application) : AndroidViewModel(app), DefaultLifecycleObserver {

    private var isFirstResume = true

    @CallSuper
    override fun onResume(owner: LifecycleOwner) {
        if (isFirstResume) {
            isFirstResume = false
            onFirstResume(owner)
        }
    }

    open fun onFirstResume(owner: LifecycleOwner) {
    }
}