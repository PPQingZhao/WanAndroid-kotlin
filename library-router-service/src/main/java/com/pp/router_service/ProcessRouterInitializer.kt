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
object ProcessRouterInitializer {

    private var mApplication: Application? = null
    fun init(application: Application) {

        if (null != mApplication) {
            throw RuntimeException("Do not initialize RouterService again")
        }
        this.mApplication = application

        // init ARouter
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(mApplication)
        // @Autowired 注解
        ARouter.getInstance().inject(this)

        mApps.add(dataBaseAppService)
        mApps.add(userAppService)
        mApps.add(networkAppService)

        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleObserver())
    }


    //        // 数据库模块
    @Autowired(name = RouterServiceImpl.DataBase.DATABASE_APP)
    lateinit var dataBaseAppService: IAppService

    // user模块
    @Autowired(name = RouterServiceImpl.User.SERVICE_APP)
    lateinit var userAppService: IAppService

    // 网络模块
    @Autowired(name = RouterServiceImpl.NetWork.SERVICE_APP)
    lateinit var networkAppService: IAppService

    private val mApps by lazy {
        ArrayList<IAppService>()
    }

    private class LifecycleObserver : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)

            // onCreate
            mApps.forEach {
                it.onCreate(mApplication!!)
            }
        }
    }

}