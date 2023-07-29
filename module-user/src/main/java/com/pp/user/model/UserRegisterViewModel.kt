package com.pp.user.model

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.pp.module_user.repositoy.UserRepository
import com.pp.network.api.WanAndroidService
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

    /*    if (password.value != confirmPassword.value) {
            helperMessage.value = "请确认密码"
            return
        }*/

        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch {
            try {
                val response = UserRepository.register(username.value, password.value,confirmPassword.value)

                val result = response.errorCode == WanAndroidService.ErrorCode.SUCCESS
                _registerResult.emit(result)

                withContext(Dispatchers.Main) {
                    helperMessage.value = response.errorMsg
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