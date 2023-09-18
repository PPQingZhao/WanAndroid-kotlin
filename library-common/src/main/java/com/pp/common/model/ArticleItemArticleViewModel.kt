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
import com.pp.common.router.MultiRouterFragmentViewModel
import com.pp.common.util.ViewTreeMultiRouterFragmentViewModel
import com.pp.common.util.getAuthor
import com.pp.common.util.getCharterName
import com.pp.common.util.getTags
import com.pp.router_service.RouterPath
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemArticleViewModel
import kotlinx.coroutines.launch

open class ArticleItemArticleViewModel(articleBean: () -> ArticleBean?, theme: AppDynamicTheme) :
    ItemArticleViewModel<ArticleBean>(theme) {

    init {
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
        ViewTreeMultiRouterFragmentViewModel.get<MultiRouterFragmentViewModel>(view)?.run {
            val data = data?.invoke() ?: return false

            val shareElement = view.findViewById<TextView>(com.pp.ui.R.id.tv_title)
            val bundle = Bundle().also {
                it.putString(WebViewFragment.WEB_VIEW_TITLE, data!!.title)
                it.putString(WebViewFragment.WEB_VIEW_URL, data!!.link)
                it.putString(
                    CommonWebViewFragment.WEB_VIEW_TRANSITION_NAME,
                    shareElement.transitionName
                )
            }

            showFragment(
                targetFragment = RouterPath.Web.fragment_web,
                tag = RouterPath.Web.fragment_web,
                arguments = bundle,
                sharedElement = shareElement
            )
        }
        return true
    }

    override fun onCollect(v: View) {
        val articleBean = data?.invoke() ?: return
        ViewTreeLifecycleOwner.get(v)?.apply {
            lifecycleScope.launch {
                if (isCollect.get()) {
                    WanAndroidService.wanApi.unCollectedArticle(articleBean.id)
                } else {
                    WanAndroidService.wanApi.collectArticle(articleBean.id)
                }.let {
                    if (it.errorCode != WanAndroidService.ErrorCode.SUCCESS) {
                        return@launch
                    }
                    isCollect.set(isCollect.get().not())
                    articleBean.collect = isCollect.get()
                }
            }
        }
    }
}