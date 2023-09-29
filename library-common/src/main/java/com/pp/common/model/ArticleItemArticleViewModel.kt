package com.pp.common.model

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pp.base.browser.WebViewFragment
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.http.wanandroid.api.WanAndroidService
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.repository.CollectedRepository
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.getAuthor
import com.pp.common.util.getCharterName
import com.pp.common.util.getTags
import com.pp.router_service.RouterPath
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class ArticleItemArticleViewModel(
    articleBean: ArticleBean?,
    theme: AppDynamicTheme,
    scope: CoroutineScope,
) :
    ItemArticleViewModel<ArticleBean>(theme) {
    init {
        scope.launch {
            CollectedRepository.collected.collectLatest {
                // 更新收藏状态
                // 收藏页面取消收藏时,更新对应 其它页面item
                if (it.originId == data?.id || it.id == data?.id) {
                    data?.collect = it.collect
                    isCollect.set(it.collect)
                }
            }
        }
        data = articleBean
    }

    override fun onUpdateData(data: ArticleBean?) {
        data.let { field ->

            author.set(data?.getAuthor())
            niceDate.set(field?.niceDate)
            title.set(Html.fromHtml(field?.title))
            chapterName.set(field?.getCharterName())
            tags.set(field?.getTags())
            transitionName.set("transitionName${field?.id}")

            isFresh.set(field?.fresh == true)
            isPinned.set(field?.type == 1)
            isCollect.set(field?.collect == true)

            link.set(field?.link)
            desc.set(field?.desc)
            envelopePic.set(field?.envelopePic)
        }
    }

    override fun onItemViewModelClick(view: View): Boolean {
        super.onItemViewModelClick(view)
        val data = data ?: return false

        val shareElement = view.findViewById<TextView>(com.pp.ui.R.id.tv_title)
        val bundle = Bundle().also {
            it.putString(WebViewFragment.WEB_VIEW_TITLE, data.title)
            it.putString(WebViewFragment.WEB_VIEW_URL, data.link)
            it.putString(
                CommonWebViewFragment.WEB_VIEW_TRANSITION_NAME,
                shareElement.transitionName
            )
        }

        MultiRouterFragmentViewModel.showFragment(
            view,
            targetFragment = RouterPath.Web.fragment_web,
            tag = RouterPath.Web.fragment_web,
            arguments = bundle,
            sharedElement = shareElement
        )
        return true
    }

    override fun onCollect(v: View) {
        val articleBean = data ?: return
        ViewTreeLifecycleOwner.get(v)?.apply {
            lifecycleScope.launch {
                val isCollected = isCollect.get()
                if (isCollected) {
                    CollectedRepository.unCollected(articleBean)
                } else {
                    CollectedRepository.collected(articleBean)
                }.let {
                    if (it.errorCode != WanAndroidService.ErrorCode.SUCCESS) {
                        return@launch
                    }
                    isCollect.set(isCollected.not())
                    articleBean.collect = isCollected.not()
                }
            }
        }
    }
}