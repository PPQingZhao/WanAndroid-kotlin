package com.pp.library_network.api

import com.pp.network.bean.ResponseBean
import com.pp.network.bean.user.LoginBean
import com.pp.network.bean.user.UserInfoBean
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * User 接口
 * ① 用户注册
 * ② 用户登录
 * ③ 获取用户信息
 * ④ 修改用户信息
 */
interface UserApi {

    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginByUserName(
        @Field("username") userName: String? = "",
        @Field("password") passWord: String? = "",
    ): ResponseBean<LoginBean>

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun registerByUserName(
        @Field("username") userName: String? = "",
        @Field("password") passWord: String? = "",
        @Field("repassword") repassword: String? = "",
    ): ResponseBean<LoginBean>

    @GET("user/logout/json")
    suspend fun logout()

    /**
     * 获取用户(token)信息
     *
     */
    @GET("user/lg/userinfo/json")
    suspend fun getUserInfo(): ResponseBean<UserInfoBean>

}