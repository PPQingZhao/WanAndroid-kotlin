package com.pp.user.ui

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.transition.MaterialSharedAxis
import com.pp.base.ThemeViewModel
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.materialSharedAxis
import com.pp.database.user.User
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.adapter.createItemViewModelBinder
import com.pp.ui.databinding.ItemAllowRightBinding
import com.pp.ui.viewModel.ItemAllowRightViewModel
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.OnItemListener
import com.pp.common.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel(app: Application) : ThemeViewModel(app) {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    val loginEnable = MutableLiveData<Boolean>(false)

    val mAdapter by lazy {
        RecyclerViewBindingAdapter<ItemAllowRightViewModel>(getItemLayoutRes = { R.layout.item_allow_right })
            .apply {

                createItemViewModelBinder<ItemAllowRightBinding, ItemAllowRightViewModel, ItemAllowRightViewModel>(
                    getItemViewModel = { it }
                ).also {
                    addItemViewModelBinder(it)
                }
            }

    }

    private fun getItems(): List<ItemAllowRightViewModel> {
        return mutableListOf<ItemAllowRightViewModel>().apply {
            val itemMessageCenter = ItemAllowRightViewModel(
                com.pp.skin.R.drawable.ic_message_center,
                R.string.message_center,
                mTheme
            )

            val itemCoin = ItemAllowRightViewModel(
                com.pp.skin.R.drawable.ic_coin,
                R.string.self_coin,
                mTheme
            ).apply {
                setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                    override fun onItemClick(
                        view: View,
                        item: ItemDataViewModel<Any>,
                    ): Boolean {
                        showFragmentWhenLogin(view, RouterPath.User.fragment_coin)
                        return true
                    }
                })
            }

            val itemCollected = ItemAllowRightViewModel(
                com.pp.skin.R.drawable.ic_favorite_on,
                R.string.self_collected,
                mTheme
            ).apply {
                setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                    override fun onItemClick(
                        view: View,
                        item: ItemDataViewModel<Any>,
                    ): Boolean {
                        showFragmentWhenLogin(view, RouterPath.User.fragment_collected)
                        return true
                    }
                })
            }

            val itemSkin = ItemAllowRightViewModel(
                com.pp.skin.R.drawable.ic_skin,
                R.string.theme_setting,
                mTheme
            ).apply {
                setOnItemListener(object : OnItemListener<ItemDataViewModel<Any>> {
                    override fun onItemClick(
                        view: View,
                        item: ItemDataViewModel<Any>,
                    ): Boolean {
                        MultiRouterFragmentViewModel.showFragment(
                            view,
                            RouterPath.Local.fragment_theme_setting,
                            RouterPath.Local.fragment_theme_setting,
                            mainExitTransition = materialSharedAxis(MaterialSharedAxis.X, true),
                            mainReenterTransition = materialSharedAxis(MaterialSharedAxis.X, false)
                        )
                        return true
                    }
                })
            }

            add(itemMessageCenter)
            add(itemCoin)
            add(itemCollected)
            add(itemSkin)
        }
    }

    private fun showFragmentWhenLogin(view: View, tagFragment: String) {
        if (user.value == null) {
            Toast.makeText(getApplication(), R.string.login_tip, Toast.LENGTH_SHORT).show()
            return
        }

        MultiRouterFragmentViewModel.showFragment(
            view, tagFragment,
            tagFragment,
            mainExitTransition = materialSharedAxis(MaterialSharedAxis.X, true),
            mainReenterTransition = materialSharedAxis(MaterialSharedAxis.X, false)
        )

    }

    override fun onCreate(owner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepository.preferenceUser().collectLatest {
                loginEnable.postValue(it == null)
                _user.postValue(it)
            }
        }
    }

    override fun onFirstResume(owner: LifecycleOwner) {
        mAdapter.setDataList(getItems())
    }

    fun nickName(user: User?): String {
        return user?.let {
            getApplication<Application>().getString(R.string.username_value)
                .format(it.nickName)
        } ?: ""
    }

    fun userId(user: User?): String {
        return user?.let {
            getApplication<Application>().getString(R.string.id_value)
                .format(it.userId)
        } ?: ""
    }

    fun coinInfo(user: User?): String {
        return user?.let {
            getApplication<Application>().getString(R.string.coin_info_value)
                .format(it.coinCount.toString(), it.level.toString(), it.rank)
        } ?: ""
    }

    /**
     * 跳转登录界面
     */
    fun onLogin(view: View) {
        showFragmentWhenLogin(view, RouterPath.User.fragment_login)
    }

    /**
     * 跳转设置界面
     */
    fun onSetting(view: View) {
        showFragmentWhenLogin(view, RouterPath.Local.fragment_setting)
    }
}