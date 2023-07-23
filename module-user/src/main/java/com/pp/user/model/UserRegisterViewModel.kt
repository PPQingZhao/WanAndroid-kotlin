package com.pp.user.model

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRegisterViewModel : RegisterViewModel(), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {

        val observer = { v: String? ->
            errorMessage.value = ""
            helperMessage.value = ""
            registerEnable.set(
                !(username.value?.isEmpty() ?: true)
                        && !(password.value?.isEmpty() ?: true)
                        && !(confirmPassword.value?.isEmpty() ?: true)
            )
        }
        username.observe(owner, observer)
        password.observe(owner, observer)
        confirmPassword.observe(owner, observer)
    }

    private val _registerResult = MutableSharedFlow<Boolean>()
    val registerResult: SharedFlow<Boolean> = _registerResult

    override fun onClick(view: View) {
        errorMessage.value = ""
        helperMessage.value = ""
        succeed.value = false

        if (password.value != confirmPassword.value) {
            helperMessage.value = "请确认密码"
            return
        }

        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch {
            try {
                // todo: 待实现
//                val response = UserRepository.register(username.value, password.value)

                val result = /*response.code == MusicService.ErrorCode.SUCCESS*/ true
                _registerResult.emit(result)

                withContext(Dispatchers.Main) {
                    helperMessage.value = /*response.msg*/""
                    succeed.value = result
                }
            } catch (e: Throwable) {
                Log.e("UserRegisterViewModel", "${e.message}")
                withContext(Dispatchers.Main) {
                    errorMessage.value = "发生错误"
                }
            }
        }
    }

    fun reset() {
        username.value = ""
        password.value = ""
        confirmPassword.value = ""
        errorMessage.value = ""
        helperMessage.value = ""
    }

    private val _onReturn = MutableSharedFlow<Boolean>()
    val onReturn: SharedFlow<Boolean> = _onReturn
    override fun onReturn(view: View) {
        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch {
            _onReturn.emit(true)
        }
        reset()
    }

}