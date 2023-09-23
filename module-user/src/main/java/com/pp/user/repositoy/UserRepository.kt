package com.pp.user.repositoy

import android.util.Log
import androidx.datastore.preferences.core.edit
import com.pp.common.app.App
import com.pp.common.constant.preferences_key_user_name
import com.pp.common.datastore.userDataStore
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.runCatchingResponse
import com.pp.common.http.wanandroid.bean.user.LoginBean
import com.pp.common.http.wanandroid.bean.user.UserInfoBean
import com.pp.database.AppDataBase
import com.pp.database.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object UserRepository {
    private const val TAG = "UserRepository"
    private val DEBUG = true

    private val userApi by lazy { WanAndroidService.userApi }
    private val userDao by lazy {
        AppDataBase.instance.getUserDao()
    }

    /**
     * 登录preference 缓存中的user
     */
    suspend fun loginPreferenceUser(): Pair<ResponseBean<LoginBean>, User?> {
        var userName: String?
        App.getInstance().baseContext.userDataStore.data.first().run {
            userName = get(preferences_key_user_name)
        }

        val user = userDao.findUser(userName ?: "")
        return try {
            login(user?.name, user?.password)
        } catch (e: Throwable) {
            Pair(ResponseBean(-1, null, e.message.toString()), user)
        }
    }

    /**
     * 成功登录后缓存到 preference
     */
    suspend fun loginWithPreferenceCache(
        userName: String?,
        password: String?,
    ): Pair<ResponseBean<LoginBean>, User?> {
        val result = login(userName, password)
        result.second?.apply {
            App.getInstance().baseContext.userDataStore.edit {
                it[preferences_key_user_name] = this.name.toString()
            }
        }
        return result
    }

    suspend fun getUserInfo(): ResponseBean<UserInfoBean> {
        return runCatchingResponse {
            userApi.getUserInfo()
        }
    }

    /**
     * 用户名&密码 登录
     */
    private suspend fun login(
        userName: String?,
        password: String?,
    ): Pair<ResponseBean<LoginBean>, User?> {

        if (DEBUG) {
            Log.v(TAG, "start login: $userName}")
        }
        // 执行登录逻辑
        val loginResponse = runCatchingResponse {
            userApi.loginByUserName(userName, password)
        }

        if (DEBUG) {
            Log.v(TAG, "login code: ${loginResponse.errorCode}")
        }
        if (loginResponse.errorCode == WanAndroidService.ErrorCode.SUCCESS) {
            val user = User(name = userName, password = password)
            val loginBean = loginResponse.data
            loginBean?.apply {
                user.token = token
            }

            // 获取uer info
            val userInfoResponse = getUserInfo()

            if (userInfoResponse.errorCode == WanAndroidService.ErrorCode.SUCCESS) {
                userInfoResponse.data?.apply {
                    user.setInfo(this)
                }
            }

            // 插入数据库
            userDao.addUser(user)
            return Pair(loginResponse, user)
        }

        return Pair(loginResponse, null)
    }

    /**
     * 用户登出,清除preference缓存的用户信息
     */
    suspend fun logoutWithPreferenceClear() {
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                userApi.logout()
            }
            App.getInstance().baseContext.userDataStore.edit {
                it[preferences_key_user_name] = ""
            }
        }
    }

    suspend fun register(
        username: String?,
        password: String?,
        rePassword: String?,
    ): ResponseBean<LoginBean> {
        return runCatchingResponse {
            userApi.registerByUserName(username, password, rePassword)
        }
    }

}