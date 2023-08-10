package com.pp.router_service

import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * user
 */
interface IUserService : IProvider {

    /**
     * 获取 登录token
     */
    fun getToken(): LiveData<String?>

    /**
     * user name
     */
    fun getNickName(): LiveData<String?>

    /**
     * 头像
     */
    fun getHeadIcon(): LiveData<String?>

    /**
     * 头像
     */
    fun getMotto(): LiveData<String?>
}