package com.pp.network.bean.user
/*
{
  "code": 0,
  "msg": "操作成功",
  "data": {
    "is_admin": 0,
    "login": {
      "e_mail": "",
      "is_login": 1,
      "last_login_ip": "223.73.201.248",
      "last_login_time": "2022-12-27 02:10:07",
      "mobile": "",
      "username": "test6"
    },
    "property": {
      "id": 98,
      "money": "0.00",
      "user_id": 98,
      "vip": 0
    },
    "user": {
      "avatar": "",
      "id": 98,
      "motto": "",
      "nick": "901672074902"
    }
  }
}

* */
data class UserInfoBean(
    val is_admin: Int = 0,
    val login: Login = Login(),
    val `property`: Property = Property(),
    val user: User = User(),
) {
    data class Login(
        val e_mail: String = "",
        val is_login: Int = -1,
        val last_login_ip: String = "",
        val last_login_time: String = "",
        val mobile: String = "",
        val username: String = "",
    )

    data class Property(
        val id: Int = 0,
        val money: String = "",
        val user_id: Int = 0,
        val vip: Int = 0,
    )

    data class User(
        val avatar: String = "",
        val id: Int = 0,
        val motto: String = "",
        val nick: String = "",
    )
}