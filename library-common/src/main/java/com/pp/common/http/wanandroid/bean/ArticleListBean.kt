package com.pp.common.http.wanandroid.bean

import android.os.Parcel
import android.os.Parcelable

data class ArticleListBean(
//    val articleList: List<ArticleBean> = listOf(),
    val articles: List<ArticleBean>? = listOf(),
    val author: String? = "",
    val children: List<ArticleListBean>? = listOf(),
    val courseId: Int = 0,
    val cover: String? = "",
    val desc: String? = "",
    val id: Int = 0,
    val lisense: String? = "",
    val lisenseLink: String? = "",
    val name: String? = "",
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val type: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int = 0,
    val cid: Long = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(ArticleBean),
        parcel.readString(),
        parcel.createTypedArrayList(CREATOR),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(articles)
        parcel.writeString(author)
        parcel.writeTypedList(children)
        parcel.writeInt(courseId)
        parcel.writeString(cover)
        parcel.writeString(desc)
        parcel.writeInt(id)
        parcel.writeString(lisense)
        parcel.writeString(lisenseLink)
        parcel.writeString(name)
        parcel.writeInt(order)
        parcel.writeInt(parentChapterId)
        parcel.writeInt(type)
        parcel.writeByte(if (userControlSetTop) 1 else 0)
        parcel.writeInt(visible)
        parcel.writeLong(cid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleListBean> {
        override fun createFromParcel(parcel: Parcel): ArticleListBean {
            return ArticleListBean(parcel)
        }

        override fun newArray(size: Int): Array<ArticleListBean?> {
            return arrayOfNulls(size)
        }
    }
}