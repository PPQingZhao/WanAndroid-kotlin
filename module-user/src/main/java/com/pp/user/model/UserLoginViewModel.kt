package com.pp.user.model

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.pp.network.api.WanAndroidService
import com.pp.user.manager.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserLoginViewModel : LoginViewModel(), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        username.value = UserManager.userModel().userName.value
        password.value = UserManager.userModel().password.value

        val observer = { v: String? ->
            errorMessage.value = ""
            helperMessage.value = ""
            enable.set(
                !(username.value?.isEmpty() ?: true)
                        && !(password.value?.isEmpty() ?: true)
            )
        }
        username.observe(owner, observer)
        password.observe(owner, observer)

    }

    private val _loginResult = MutableSharedFlow<Boolean>()
    val loginResult: SharedFlow<Boolean> = _loginResult

    override fun onClick(view: View) {
        succeed.value = false
        errorMessage.value = ""
        helperMessage.value = ""
        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch(Dispatchers.IO) {

            try {
                val response = UserManager.loginWithPreferenceCache(username.value, password.value)
                val result = response.errorCode == WanAndroidService.ErrorCode.SUCCESS

                _loginResult.emit(result)
                withContext(Dispatchers.Main) {
                    helperMessage.value = response.errorMsg
                    succeed.value = result
                }
            } catch (e: Throwable) {
                Log.e("UserLoginViewModel", "${e.message}")
                withContext(Dispatchers.Main) {
                    errorMessage.value = "发生错误"
                }
            }
        }
    }

    private val _onNewUser = MutableSharedFlow<Boolean>()
    val onNewUser: SharedFlow<Boolean> = _onNewUser
    override fun onNewUser(view: View) {
        ViewTreeLifecycleOwner.get(view)?.lifecycleScope?.launch {
            _onNewUser.emit(true)
        }
    }
}