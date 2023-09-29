package com.pp.user.model

import android.view.View
import androidx.lifecycle.*
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserLoginViewModel : LoginViewModel(), DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        owner.lifecycleScope.launch(Dispatchers.IO) {
            UserRepository.preferenceUser().collectLatest {
                username.postValue(it?.nickName)
                password.postValue(it?.password)
            }
        }

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
            val response =
                UserRepository.loginWithPreferenceCache(username.value, password.value).first
            val result = response.errorCode == WanAndroidService.ErrorCode.SUCCESS

            _loginResult.emit(result)
            withContext(Dispatchers.Main) {
                helperMessage.value = response.errorMsg
                succeed.value = result
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