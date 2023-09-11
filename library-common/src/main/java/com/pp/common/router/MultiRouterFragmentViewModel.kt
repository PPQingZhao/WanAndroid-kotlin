package com.pp.common.router

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pp.base.ThemeViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MultiRouterFragmentViewModel(app: Application) : ThemeViewModel(app) {

    private val _popBackStack = MutableSharedFlow<String>()
    val popBackStack = _popBackStack.asSharedFlow()

    private val _showFragment = MutableSharedFlow<RouterInfo>()
    val showFragment = _showFragment.asSharedFlow()

    fun popBackStack(fragmentTag: String) {
        viewModelScope.launch {
            _popBackStack.emit(fragmentTag)
        }
    }

    fun showFragment(
        targetFragment: String,
        tag: String?,
        arguments: Bundle? = null,
        mainExitTransition: Any? = null,
        mainReenterTransition: Any? = null,
        sharedElement: View? = null,
    ) {
        viewModelScope.launch {
            _showFragment.emit(
                RouterInfo(
                    targetFragment,
                    tag,
                    arguments,
                    mainExitTransition,
                    mainReenterTransition,
                    sharedElement
                )
            )
        }
    }

    data class RouterInfo(
        val targetFragment: String,
        val tag: String? = null,
        val arguments: Bundle? = null,
        val mainExitTransition: Any? = null,
        val mainReenterTransition: Any? = null,
        val sharedElement: View? = null,
    )
}