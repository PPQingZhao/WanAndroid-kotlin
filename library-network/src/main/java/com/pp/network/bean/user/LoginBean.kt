package com.pp.network.bean.user

open class LoginBean(
    val `property`: Property = Property(),
    val token: String = "",
    val user: User = User(),
    val user_id: Int = 0,
) {
    data class Property(
        val is_admin: Int = 0,
    )

    data class User(
        val avatar: String = "",
        val id: Int = 0,
        val motto: String = "",
        val nick: String = "",
    )
}