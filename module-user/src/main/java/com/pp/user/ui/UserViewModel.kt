package com.pp.user.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.pp.base.ThemeViewModel
import com.pp.database.user.User
import com.pp.user.manager.UserManager

class UserViewModel(app: Application) : ThemeViewModel(app) {
    val userModel = UserManager.userModel()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.e("TAG", "onCreate: ${coinInfo(userModel.user)}")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.e("TAG", "${userModel.nickName.value}")
        Log.e("TAG", coinInfo(userModel.user))
    }

    fun nickName(nickName: String?): String {
        return "用户名: $nickName"
    }

    fun userId(user: User?): String {
        return "id: ${user?.userId}"
    }

    fun coinInfo(user: User?): String {
        return "积分:${user?.coinCount} 等级:${user?.level} 排名:${user?.rank}"
    }
}