package com.pp.network.bean.user
/*
{
		"coinInfo": {
			"coinCount": 10,
			"level": 1,
			"nickname": "",
			"rank": "9999+",
			"userId": 151418,
			"username": "p**test"
		},
		"collectArticleInfo": {
			"count": 0
		},
		"userInfo": {
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
	}

*/

data class UserInfoBean(
    val coinInfo: CoinInfo = CoinInfo(),
    val collectArticleInfo: CollectArticleInfo = CollectArticleInfo(),
    val userInfo: LoginBean = LoginBean(),
) {
    data class CoinInfo(
        val coinCount: Long = 0,
        val level: Long = 0,
        val nickname: String = "",
        val rank: String = "",
        val userId: Long = 0,
        val username: String = "",
    )

    data class CollectArticleInfo(
        val count: Long = 0,
    )

}