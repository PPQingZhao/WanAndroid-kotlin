package com.pp.common.model

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.pp.base.browser.WebViewFragment
import com.pp.common.app.App
import com.pp.common.browser.CommonWebViewFragment
import com.pp.common.http.wanandroid.bean.ArticleBean
import com.pp.common.util.ShareElementNavigation
import com.pp.common.util.getAuthor
import com.pp.common.util.getCharterName
import com.pp.common.util.getTags
import com.pp.router_service.RouterPath
import com.pp.theme.AppDynamicTheme
import com.pp.ui.viewModel.ItemArticleViewModel

open class ArticleItemArticleViewModel(articleBean: ArticleBean?, theme: AppDynamicTheme) :
    ItemArticleViewModel<ArticleBean>(theme) {

    init {
        data = articleBean
    }

    override fun onUpdateData(data: ArticleBean?) {
        data.let { field ->
            author.set(data?.getAuthor())
            niceDate.set(field?.niceDate)
            title.set(field?.title)
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


    override fun onItemClick(view: View) {
        if (null == data) {
            return
        }

        val shareElement = view.findViewById<TextView>(com.pp.ui.R.id.tv_title)
        val bundle = Bundle().also {
            it.putString(WebViewFragment.WEB_VIEW_TITLE, title.get())
            it.putString(WebViewFragment.WEB_VIEW_URL, data!!.link)
            it.putString(
                CommonWebViewFragment.WEB_VIEW_TRANSITION_NAME,
                shareElement.transitionName
            )
        }

        App.getInstance().navigation.value =
            RouterPath.Web.fragment_web to ShareElementNavigation(shareElement, bundle)
    }

    override fun onCollect(v: View) {
        isCollect.let {
            it.set(it.get().not())
            data?.collect = it.get()
        }
    }
}