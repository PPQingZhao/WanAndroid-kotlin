package com.pp.user.repositoy

import com.pp.database.user.User
import com.pp.network.bean.user.UserInfoBean

fun User.setInfo(info: UserInfoBean?) {
    name = info?.userInfo?.username
    e_mail = info?.userInfo?.email
    nickName = info?.userInfo?.nickname
    avatar = info?.userInfo?.icon
}