package com.pp.common.http.wanandroid.api

import com.pp.library_network.api.UserApi
import com.pp.network.utils.RetrofitUtil
import com.pp.network.utils.addHeadersInterceptor
import com.pp.network.utils.bodyLoggingInterceptor

interface WanAndroidService {

    companion object {

        private val header = mutableMapOf<String, String>()
        private const val URL_BASE = "https://www.wanandroid.com/"

        private val retrofit by lazy {
            RetrofitUtil.create(
                URL_BASE,
                bodyLoggingInterceptor(),
                addHeadersInterceptor(header)
            )
        }

        /**
         * api impl
         */
        val collectedApi: CollectedApi by lazy { retrofit.create(CollectedApi::class.java) }
        val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
        val homeApi: HomeApi by lazy { retrofit.create(HomeApi::class.java) }
        val projectApi: ProjectApi by lazy { retrofit.create(ProjectApi::class.java) }
        val navigationApi: NavigationApi by lazy { retrofit.create(NavigationApi::class.java) }
        val wxArticleApi: WXArticleApi by lazy { retrofit.create(WXArticleApi::class.java) }
        val systemApi: SystemApi by lazy { retrofit.create(SystemApi::class.java) }
        val searchApi: SearchApi by lazy { retrofit.create(SearchApi::class.java) }
        val coinApi: CoinApi by lazy { retrofit.create(CoinApi::class.java) }

    }

    object ErrorCode {
        const val FAILED = -1
        const val SUCCESS = 0
        const val LOGIN_FAILED = -1001
    }

}