package com.pp.common.router

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.pp.base.ThemeActivity
import com.pp.common.R
import com.pp.common.databinding.ActivityMultiRouterFragmentBinding
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 单activity 管理多个路由Fragment
 *
 * 启动MultiRouterFragmentActivity时,需要通过 @see [startMultiRouterFragmentActivity] 设置 main fragment
 *
 */
open class MultiRouterFragmentActivity :
    ThemeActivity<ActivityMultiRouterFragmentBinding, MultiRouterFragmentViewModel>() {
    override val mBinding: ActivityMultiRouterFragmentBinding by lazy {
        ActivityMultiRouterFragmentBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<MultiRouterFragmentViewModel> {
        return MultiRouterFragmentViewModel::class.java
    }

    private val DEBUG = false

    companion object {
        const val MAIN_FRAGMENT = "main_fragment"

        inline fun <reified RouterActivity:MultiRouterFragmentActivity> startMultiRouterFragmentActivity(context: Context, targetFragment: String) {
            context.startActivity(
                Intent(context, RouterActivity::class.java).apply {
                    putExtra(MAIN_FRAGMENT, targetFragment)
                }
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewTreeMultiRouterFragmentViewModel.set(mBinding.root, mViewModel)

        // 监听 fragment resumed 状态,更新 showing fragment
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                /**
                 * 注意:
                 * 使用 @see [FragmentTransaction.hide] @see [FragmentManager.popBackStackImmediate]情况下,
                 *  @see[Fragment.isVisible] 不能判断当前resumed fragment是否可见
                 */
                if (null != f.view?.parent && !f.isHidden) {
                    mShowingFragment = f
                }

                if (DEBUG) {
                    Log.e("RouterContainerActivity", "showingFragment: $mShowingFragment")
                }
            }
        }, false)

        parseMainIntent()

        lifecycleScope.launch {
            async {
                mViewModel.popBackStack.collectLatest {
                    popBackStack(it)
                }
            }

            async {
                mViewModel.showFragment.collectLatest {
                    if (tagMainFragment == mShowingFragment?.tag) {
                        mShowingFragment?.exitTransition = it.mainExitTransition
                        mShowingFragment?.reenterTransition = it.mainReenterTransition
                    }
                    val targetFragment =
                        ARouter.getInstance().build(it.targetFragment).navigation() as Fragment
                    targetFragment.arguments = it.arguments
                    showFragment(targetFragment, it.tag, it.sharedElement, true)
                }
            }
        }

    }

    private var tagMainFragment: String? = null

    /**
     * 解析intent, 设置main fragment
     */
    private fun parseMainIntent() {

        val routerMainFragment = intent?.getStringExtra(MAIN_FRAGMENT)
        if (routerMainFragment?.isEmpty() == true) {
            throw RuntimeException("you must set ‘main fragment’.")
        }

        val mainFragment = ARouter.getInstance().build(routerMainFragment).navigation() as Fragment
        tagMainFragment = routerMainFragment
        showFragment(mainFragment, tag = tagMainFragment, addToBackStack = false)
    }

    /**
     * fragment 出栈
     */
    @SuppressLint("CommitTransaction")
    private fun popBackStack(popTag: String) {
        if (popTag != mShowingFragment?.tag) {
            if (DEBUG) {
                Log.e(
                    "RouterContainerActivity",
                    "popBackStack failed:{pop:$popTag},showing:${mShowingFragment?.tag}"
                )
            }
            return
        }
        supportFragmentManager.popBackStackImmediate()
    }

    /**
     * 记录当前展示的fragment
     */
    private var mShowingFragment: Fragment? = null

    /**
     * material motion转场动画:
     *  单独使用 @see [FragmentTransaction.add]没有动画效果,
     *  可以配合 @see [FragmentTransaction.hide]使用,使动画效果生效
     *  或者使用 @see [FragmentTransaction.replace]使动画生效
     *
     * 在这里使用  @see [FragmentTransaction.add]方式配合 @see [FragmentTransaction.hide]使转场动画生效,
     * 并且使用 @see [FragmentTransaction.setMaxLifecycle]控制fragment生命周期
     * 值得注意的是 @see [FragmentTransaction.hide]之后,再次显示时不是使用@see [FragmentTransaction.show]时,
     * 比如:按下回退键(系统处理fragment退栈  @see [FragmentManager.popBackStackImmediate])
     * 会导致 @see [Fragment.isVisible]为false,就不能使用该属性判断fragment是否可见([mShowingFragment]的更新判断就不可以使用该条件)
     */
    @SuppressLint("CommitTransaction")
    private fun showFragment(
        fragment: Fragment,
        tag: String?,
        sharedElement: View? = null,
        addToBackStack: Boolean = true,
    ) {
        supportFragmentManager.beginTransaction().let { transaction ->

            val oldFragment = mShowingFragment
            mShowingFragment = null
            oldFragment?.let {
                transaction.hide(it)
                if (oldFragment.isAdded) {
                    transaction.setMaxLifecycle(it, Lifecycle.State.STARTED)
                }
            }

            fragment.let { f ->
                if (!f.isAdded) {
                    transaction.add(R.id.container, f, tag)
                }

                if (null != sharedElement) {
                    transaction.addSharedElement(sharedElement, sharedElement.transitionName)
                }

                if (addToBackStack) {
                    transaction.addToBackStack("main")
                }

                transaction
                    .show(f)
                    .setMaxLifecycle(f, Lifecycle.State.RESUMED)
                    .commitAllowingStateLoss()
            }
        }
    }

}
