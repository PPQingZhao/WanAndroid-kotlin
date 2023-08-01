package com.pp.router_service

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 路由初始化
 * 分发app生命周期给各模块
 */
class RouterInitializer private constructor() {

    private fun init() {
        // init ARouter
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(mApplication)
        // @Autowired 注解
        ARouter.getInstance().inject(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleObserver(this))
    }

    companion object {

        private var mApplication: Application? = null
        fun init(application: Application) {

            if (null != mApplication) {
                throw RuntimeException("Do not initialize RouterService again")
            }
            mApplication = application

            RouterInitializer().init()
        }
    }

    // 数据库模块
    @Autowired(name = RouterServiceImpl.DataBase.DATABASE_APP)
    lateinit var dataBaseAppService: IAppService

    // user模块
    @Autowired(name = RouterServiceImpl.User.USER_APP)
    lateinit var userAppService: IAppService

    // 网络模块
    @Autowired(name = RouterServiceImpl.NetWork.NETWORK_APP)
    lateinit var networkAppService: IAppService

    // theme模块
    @Autowired(name = RouterServiceImpl.Theme.THEME_APP)
    lateinit var themeAppService: IAppService

    private val mApps by lazy {
        ArrayList<IAppService>().apply {
            add(dataBaseAppService)
            add(userAppService)
            add(networkAppService)
            add(themeAppService)
        }
    }

    private class LifecycleObserver(private val routerInitializer: RouterInitializer) :
        DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)

            // onCreate
            routerInitializer.mApps.forEach {
                it.onCreate(mApplication!!)
            }
        }
    }

}