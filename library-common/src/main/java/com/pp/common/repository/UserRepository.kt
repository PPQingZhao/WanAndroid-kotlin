package com.pp.common.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pp.common.app.App
import com.pp.common.constant.preferences_key_user_name
import com.pp.common.datastore.userDataStore
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ResponseBean
import com.pp.common.http.wanandroid.bean.runCatchingResponse
import com.pp.common.http.wanandroid.bean.user.LoginBean
import com.pp.common.http.wanandroid.bean.user.UserInfoBean
import com.pp.common.util.getSingleDataWhenResume
import com.pp.database.AppDataBase
import com.pp.database.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object UserRepository {
    private const val TAG = "UserRepository"
    private val DEBUG = true

    private val userApi by lazy { WanAndroidService.userApi }
    private val userDao by lazy {
        AppDataBase.instance.value!!.getUserDao()
    }

    private fun getUserDataStore(): DataStore<Preferences> {
        return App.getInstance().baseContext.userDataStore
    }

    suspend fun getPreferenceUser(block:suspend (user: User?) -> Unit) {
        getUserPreferences().collectLatest {
            val userName = it[preferences_key_user_name]
            val user = findUser(userName)
            block.invoke(user)
        }
    }

    suspend fun getPreferenceUser(): User? {
        val userName = getUserPreferences().first()[preferences_key_user_name]
        return findUser(userName)
    }

    suspend fun getPreferenceUserName(block: (userName: String?) -> Unit) {
        getUserPreferences().collectLatest {
            val userName = it[preferences_key_user_name]
            block.invoke(userName)
        }
    }

    private fun getUserPreferences(): Flow<Preferences> {
        return getUserDataStore().data
    }

    suspend fun findUser(name: String?): User? {
        return userDao.findUser(name)
    }

    /**
     * 登录preference 缓存中的user
     */
    suspend fun loginPreferenceUser(): Pair<ResponseBean<LoginBean>, User?> {
        val userName: String? = getUserPreferences().first()[preferences_key_user_name]

        val user = findUser(userName ?: "")
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
            getUserDataStore().edit {
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
            getUserDataStore().edit {
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

/**
 * 监听 Preference中user更新变化,并且在Resume状态通知更新的user
 */
fun UserRepository.getPreferenceUserWhenResume(
    owner: LifecycleOwner,
    block: (user: User?) -> Unit,
) {
    owner.lifecycleScope.launch(Dispatchers.IO) {
        owner.getSingleDataWhenResume(
            getPreferenceUser(),
            getData = { dataFlow ->
                getPreferenceUser {
                    dataFlow.emit(it)
                }
            }).collectLatest {
            block.invoke(it)
        }
    }
}