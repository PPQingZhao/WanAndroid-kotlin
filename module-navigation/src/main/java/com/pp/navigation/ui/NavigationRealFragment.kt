package com.pp.navigation.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pp.base.ThemeFragment
import com.pp.common.http.wanandroid.bean.ArticleCidBean
import com.pp.navigation.databinding.FragmentRealnavigationBinding
import com.pp.navigation.model.ItemArticleTextViewModel
import com.pp.navigation.model.ItemCidTextViewModel
import com.pp.ui.adapter.RecyclerViewBindingAdapter
import com.pp.ui.databinding.ItemTagFlexboxBinding
import com.pp.ui.databinding.ItemText2Binding
import com.pp.ui.databinding.ItemTextBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavigationRealFragment private constructor() :
    ThemeFragment<FragmentRealnavigationBinding, NavigationRealViewModel>() {
    override val mBinding: FragmentRealnavigationBinding by lazy {
        FragmentRealnavigationBinding.inflate(layoutInflater)
    }

    override fun getModelClazz(): Class<NavigationRealViewModel> {
        return NavigationRealViewModel::class.java
    }

    companion object {
        fun newInstance(): NavigationRealFragment {
            return NavigationRealFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ItemCidTextViewModel.selectedItemModel.observe(viewLifecycleOwner) {
            (mBinding.articleRecyclerview.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                it.getPosition(),
                0
            )
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.cidRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        mBinding.cidRecyclerview.adapter = cidAdapter

        mBinding.articleRecyclerview.layoutManager = LinearLayoutManager(requireContext()).also {
//            it.flexDirection = FlexDirection.ROW
//            it.flexWrap = FlexWrap.WRAP
//            it.justifyContent = JustifyContent.FLEX_START
        }
        mBinding.articleRecyclerview.adapter = articleAdapter
    }

    private val cidAdapter =
        RecyclerViewBindingAdapter.DefaultRecyclerViewBindingAdapter<ItemTextBinding, ItemCidTextViewModel, ArticleCidBean>(
            onCreateBinding = {
                ItemTextBinding.inflate(layoutInflater, it, false)
            },
            onBindItemModel = { _, item, position, cachedItemModel ->
//                Log.e("TAG","position: $position  $item")
                if (cachedItemModel is ItemCidTextViewModel) {
                    cachedItemModel.apply { updateData(item) }
                } else {
                    ItemCidTextViewModel(item, mViewModel.mTheme)
                }.apply {
                    setPosition(position)
                }
            }
        )

    private val articleAdapter =
        RecyclerViewBindingAdapter.DefaultRecyclerViewBindingAdapter<ItemTagFlexboxBinding, ItemCidTextViewModel, ArticleCidBean>(
            onCreateBinding = {
                ItemTagFlexboxBinding.inflate(layoutInflater, it, false)
            },
            onBindItemModel = { bind, item, position, cachedItemModel ->
                Log.e("TAG", "position: $position  $item")

                bind.flexLayout.removeAllViews()
                item?.articles?.onEach { article ->
                    ItemText2Binding.inflate(layoutInflater, bind.flexLayout, false)
                        .let {
                            it.viewModel = ItemArticleTextViewModel(article, mViewModel.mTheme)
                            bind.flexLayout.addView(it.root)
                        }
                }

                if (cachedItemModel is ItemCidTextViewModel) {
                    cachedItemModel.apply { updateData(item) }
                } else {
                    ItemCidTextViewModel(item, mViewModel.mTheme)
                }
            }
        )
    /* RecyclerViewBindingAdapter.DefaultRecyclerViewBindingAdapter<ItemTagFlexboxBinding, ItemCidTextViewModel, ArticleCidBean>(
         onCreateBinding = {
//                Log.e("TAG", "onCreateBinding")
             ItemTagFlexboxBinding.inflate(layoutInflater, it, false)
         },
         onBindItemModel = { bind, item, _, cachedItemModel ->

             bind.flexLayout.removeAllViews()
             item?.articles?.onEach {
                 ItemText2Binding.inflate(layoutInflater, bind.flexLayout, false).run {
                     viewModel = ItemArticleTextViewModel(it, mViewModel.mTheme)
                     bind.flexLayout.addView(root)
                 }
             }
             if (cachedItemModel is ItemCidTextViewModel) {
                 cachedItemModel.updateData(item)
                 cachedItemModel
             } else {
                 ItemCidTextViewModel(item, mViewModel.mTheme)
             }
         }
     )
*/

    override fun onFirstResume() {
        lifecycleScope.launch {
            async {
                mViewModel.navigation.collectLatest {
                    cidAdapter.setDataList(it)
                    articleAdapter.setDataList(it)
                }
            }

//            async {
//                mViewModel.articles.collectLatest {
//                }
//            }
        }
        mViewModel.getNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ItemCidTextViewModel.reset()
    }

}