package com.pp.common.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun LifecycleOwner.launchAndCollectIn(
    scope: CoroutineScope = lifecycleScope,
    context: CoroutineContext = EmptyCoroutineContext,
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit,
) {
    scope.launch(context) {
        repeatOnLifecycle(state, block)
    }
}

/**
 * resume状态接收防抖数据
 *
 * 适用于重新回到界面(resume)时,处理在后台加载的数据
 */
fun <T> LifecycleOwner.getSingleDataWhenResume(
    initData: T?,
    getData: suspend (dataFlow: MutableStateFlow<T?>) -> Unit,
): SharedFlow<T?> {
    var data = initData
    val dataFlow = MutableStateFlow<T?>(data)

    val callbackFlow = MutableSharedFlow<T?>()
    launchAndCollectIn(
        state = Lifecycle.State.RESUMED
    ) {
        dataFlow.collectLatest {
            if (data == it) return@collectLatest
            data = it
            callbackFlow.emit(it)
        }
    }

    lifecycleScope.launch(Dispatchers.IO) {
        getData.invoke(dataFlow)
    }
    return callbackFlow
}