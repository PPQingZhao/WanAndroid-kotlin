package com.pp.main

import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.WindowInsets
import androidx.lifecycle.ViewModelProvider
import com.pp.common.router.MultiRouterFragmentActivity
import com.pp.main.databinding.ActivityMainBinding

class MainActivity : MultiRouterFragmentActivity() {

    private val mMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMainBinding.viewModel = ViewModelProvider(this)[MainViewModel::class.java].apply {
            lifecycle.addObserver(this)
        }
        mMainBinding.lifecycleOwner = this
        addContentView(
            mMainBinding.container,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
    }

    override fun onDispatchWindowInsets(insets: WindowInsets) {
        mMainBinding.container.dispatchApplyWindowInsets(insets)
    }

}