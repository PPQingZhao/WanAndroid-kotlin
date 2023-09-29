package com.pp.navigation.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleListBean
import com.pp.common.model.ItemSelectedModel
import com.pp.common.paging.itemText1ArticleListBinder
import com.pp.common.paging.itemText2ArticleListBinder
import com.pp.common.paging.itemText3ArticleBinder
import com.pp.common.repository.UserRepository
import com.pp.common.repository.getPreferenceUserWhenResume
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.showResponse
import com.pp.navigation.databinding.FragmentSystemBinding
import com.pp.router_service.RouterPath
import com.pp.ui.R
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.utils.StateView
import com.pp.ui.viewModel.ItemDataViewModel
import com.pp.ui.viewModel.ItemTextViewModel
import com.pp.ui.viewModel.OnItemListener
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class SystemFragment private constructor() :
    ThemeFragment<FragmentSystemBinding, SystemViewModel>() {
    override val mBinding: FragmentSystemBinding by lazy {
        FragmentSystemBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<SystemViewModel> {
        return SystemViewModel::class.java
    }

    companion object {
        fun newInstance(): SystemFragment {
            return SystemFragment()
        }
    }

    override fun onFirstResume() {

        lifecycleScope.launch {
            async {
                var isInit = true
                mViewModel.systemList.collectLatest { it ->
                    if (isInit) {
                        mStateView.showLoading()
                        isInit = false
                        return@collectLatest
                    }
                    if (it.isEmpty()) {
                        mStateView.showEmpty()
                    } else {
                        mStateView.showContent()
                    }
                    mAdapter.setDataList(it)
                }
            }

            async {
                mViewModel.articleList.collectLatest {
                    mBinding.articleListRecyclerview.setItemViewCacheSize(it.size)
                    mArticleListAdapter.setDataList(it)
                }
            }
        }

        getSystemList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserRepository.getPreferenceUserWhenResume(viewLifecycleOwner) {
            getSystemList()
        }
        initRecyclerview()
    }


    private val mStateView by lazy {

        StateView.DefaultBuilder(
            mBinding.contentParent,
            mViewModel.mTheme,
            viewLifecycleOwner
        ).setOnRetry {
            getSystemList()
        }.build()

    }

    private fun getSystemList() {
        mStateView.showLoading()
        viewLifecycleOwner.lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                mViewModel.getSystemList()
            }
            mStateView.showResponse(response)
        }
    }

    private val selectedItem: ItemSelectedModel<ArticleListBean, ItemTextViewModel<ArticleListBean>> =
        ItemSelectedModel()

    private val mOnItemListener = object :
        OnItemListener<ItemDataViewModel<ArticleListBean>> {
        override fun onItemClick(
            view: View,
            item: ItemDataViewModel<ArticleListBean>,
        ): Boolean {
            selectedItem.selectedItem(item as ItemTextViewModel<ArticleListBean>)
            return true
        }
    }

    private val mAdapter by lazy {
        RecyclerViewBindingAdapter<ArticleListBean>(getItemLayoutRes = { R.layout.item_text3 })
            .apply {
                itemText3ArticleBinder(
                    onItemListener = mOnItemListener,
                    theme = mViewModel.mTheme,
                    onBindViewModel = { _, viewModel, position, _ ->
                        if (selectedItem.getSelectedItem() == null && position == 0) {
                            selectedItem.selectedItem(viewModel)
                        }
                        false
                    }).also {
                    addItemViewModelBinder(it)
                }
            }
    }

    private val mArticleListAdapter by lazy {

        val onItemListener = object : OnItemListener<ItemDataViewModel<ArticleListBean>> {
            override fun onItemClick(
                view: View, item: ItemDataViewModel<ArticleListBean>,
            ): Boolean {
                var systemArticleList: ArticleListBean? = null
                var targetPosition = 0
                lifecycleScope.launch {

                    mViewModel.systemList.collectLatest {
                        it.onEach { articleListBean ->
                            val data = item.data
                            if (articleListBean.children?.contains(data) == true) {
                                systemArticleList = articleListBean
                                targetPosition = articleListBean.children!!.indexOf(data)
                                return@onEach
                            }
                        }
                        cancel()
                    }
                }
                val bundle = Bundle().apply {
                    putParcelable(TabSystemFragment.SYSTEM_ARTICLE_LIST, systemArticleList)
                    putInt(TabSystemFragment.TARGET_POSITION, targetPosition)
                }

                MultiRouterFragmentViewModel.showFragment(
                    mBinding.root,
                    targetFragment = RouterPath.Navigation.fragment_tab_system,
                    tag = RouterPath.Navigation.fragment_tab_system,
                    arguments = bundle
                )

                return true
            }
        }

        RecyclerViewBindingAdapter<ArticleListBean>(getItemLayoutRes = {
            if (it!!.parentChapterId == 0) {
                R.layout.item_text1
            } else {
                R.layout.item_text2
            }
        }).apply {
            itemText1ArticleListBinder(
                theme = mViewModel.mTheme
            ).also {
                addItemViewModelBinder(it)
            }

            itemText2ArticleListBinder(
                onItemListener = onItemListener,
                theme = mViewModel.mTheme,
            ).also {
                addItemViewModelBinder(it)
            }
        }
    }

    private fun initRecyclerview() {
        mBinding.systemRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).let {
            mBinding.systemRecyclerview.addItemDecoration(it)
        }
        mBinding.systemRecyclerview.adapter = mAdapter

        mBinding.articleListRecyclerview.layoutManager =
            FlexboxLayoutManager(requireContext()).apply {
                isAutoMeasureEnabled = false
            }
        mBinding.articleListRecyclerview.adapter = mArticleListAdapter

        selectedItem.observerSelectedItem(
            viewLifecycleOwner
        ) { item ->
            item?.run {
                lifecycleScope.launch {
                    mViewModel.articleList.collectLatest {
                        val pos = it.indexOf(item.data as Any)
                        mBinding.articleListRecyclerview.scrollToPosition(pos)
                        cancel()
                    }
                }
            }
        }

    }
}