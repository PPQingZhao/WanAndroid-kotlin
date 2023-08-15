package com.pp.user.repositoy

import com.pp.database.user.User
import com.pp.common.http.wanandroid.bean.user.UserInfoBean

fun User.setInfo(info: UserInfoBean?) {
    name = info?.userInfo?.username
    e_mail = info?.userInfo?.email
    nickName = info?.userInfo?.nickname
    avatar = info?.userInfo?.icon
    coinCount = info?.coinInfo?.coinCount
    level =info?.coinInfo?.level
    rank = info?.coinInfo?.rank
    userId = info?.userInfo?.id

}