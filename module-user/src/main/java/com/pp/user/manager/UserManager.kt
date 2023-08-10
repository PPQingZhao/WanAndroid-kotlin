package com.pp.user.manager

import android.content.Context
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import com.pp.database.user.User
import com.pp.user.repositoy.UserModel
import com.pp.module_user.repositoy.UserRepository
import com.pp.network.bean.ResponseBean
import com.pp.network.bean.user.LoginBean
import com.pp.router_service.IUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserManager : IUserService {

    private const val TAG = "UserManager"
    private val userModel = UserModel()

    fun userModel(): UserModel {
        return userModel
    }

    suspend fun loginPreferenceUser() {
        val result = UserRepository.loginPreferenceUser()
        withContext(Dispatchers.Main) {
            processLoginResult(result)
        }
    }

    suspend fun loginWithPreferenceCache(
        userName: String?,
        password: String?,
    ): ResponseBean<LoginBean> {
        // 先登出
        logoutWithPreferenceClear()
        // 登录用户
        val loginPair = UserRepository.loginWithPreferenceCache(userName, password)
        withContext(Dispatchers.Main) {
            processLoginResult(loginPair)
        }
        return loginPair.first
    }

    @UiThread
    private fun processLoginResult(loginPair: Pair<ResponseBean<LoginBean>, User?>) {
        // 不管登录是否成功,都要更新user
        // preference 登录成功都会返回记录的user
        userModel.user = loginPair.second
    }

    suspend fun logoutWithPreferenceClear() {
        withContext(Dispatchers.Main) {
            userModel.user = null
        }
        return UserRepository.logoutWithPreferenceClear()
    }

    override fun getToken(): LiveData<String?> {
        return userModel.loginToken
    }

    override fun getNickName(): LiveData<String?> {
        return userModel.nickName
    }

    override fun getHeadIcon(): LiveData<String?> {
        return userModel.headIcon
    }

    override fun getMotto(): LiveData<String?> {
        return userModel.motto
    }

    override fun init(context: Context?) {
    }
}