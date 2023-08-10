package com.pp.network.bean.user

/*
{
		"admin": false,
		"chapterTops": [],
		"coinCount": 10,
		"collectIds": [],
		"email": "",
		"icon": "",
		"id": 151418,
		"nickname": "pp_test",
		"password": "",
		"publicName": "pp_test",
		"token": "",
		"type": 0,
		"username": "pp_test"
	}
 */
data class LoginBean(
    val admin: Boolean = false,
    val chapterTops: List<Any> = listOf(),
    val coinCount: Int = 0,
    val collectIds: List<Any> = listOf(),
    val email: String = "",
    val icon: String = "",
    val id: Long = 0,
    val nickname: String = "",
    val password: String = "",
    val publicName: String = "",
    val token: String = "",
    val type: Int = 0,
    val username: String = ""
)