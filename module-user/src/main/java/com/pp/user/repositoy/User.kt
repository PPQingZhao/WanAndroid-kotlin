package com.pp.user.repositoy

import com.pp.database.user.User
import com.pp.network.bean.user.UserInfoBean

fun User.setInfo(info: UserInfoBean?) {
    name = info?.login?.username
    e_mail = info?.login?.e_mail
    mobile = info?.login?.mobile
    nickName = info?.user?.nick
    motto = info?.user?.motto
    avatar = info?.user?.avatar
}