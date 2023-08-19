package com.pp.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PageDataListView : RecyclerView {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    fun <Data : Any> setPageAdapter(
        lifecycleOwner: LifecycleOwner,
        lifecycleScope: LifecycleCoroutineScope,
        pageData: Flow<PagingData<Data>>,
        pagingAdapter: PagingDataAdapter<Data, out ViewHolder>,
    ) {

        layoutManager = LinearLayoutManager(context)
        adapter = pagingAdapter

        lifecycleScope.launch(Dispatchers.IO) {
            pageData.collect {
                pagingAdapter.submitData(lifecycleOwner.lifecycle, it)
            }
        }
    }
}