package com.pp.common.http.wanandroid.bean

import android.os.Parcel
import android.os.Parcelable

/*
    {
				"adminAdd": false,
				"apkLink": "",
				"audit": 1,
				"author": "",
				"canEdit": false,
				"chapterId": 502,
				"chapterName": "自助",
				"collect": false,
				"courseId": 13,
				"desc": "",
				"descMd": "",
				"envelopePic": "",
				"fresh": false,
				"host": "",
				"id": 27001,
				"isAdminAdd": false,
				"link": "https://mp.weixin.qq.com/s/4eWuPTEw4Sgpi2VlBwWCUw",
				"niceDate": "2天前",
				"niceShareDate": "2天前",
				"origin": "",
				"prefix": "",
				"projectLink": "",
				"publishTime": 1691994344000,
				"realSuperChapterId": 493,
				"selfVisible": 0,
				"shareDate": 1691994344000,
				"shareUser": "小马快跑",
				"superChapterId": 494,
				"superChapterName": "广场Tab",
				"tags": [],
				"title": "超能力文本：探索Span机制的多彩世界（一）",
				"type": 0,
				"userId": 128797,
				"visible": 1,
				"zan": 0
			}
 */
data class ArticleBean(
    val adminAdd: Boolean = false,
    val apkLink: String? = "",
    val audit: Int = 0,
    val author: String? = "",
    val canEdit: Boolean = false,
    val chapterId: Int = 0,
    val chapterName: String? = "",
    var collect: Boolean = false,
    val courseId: Int = 0,
    val desc: String? = "",
    val descMd: String? = "",
    val envelopePic: String? = "",
    val fresh: Boolean = false,
    val host: String? = "",
    val id: Int = 0,
    val isAdminAdd: Boolean = false,
    val link: String? = "",
    val niceDate: String? = "",
    val niceShareDate: String? = "",
    val origin: String? = "",
    val prefix: String? = "",
    val projectLink: String? = "",
    val publishTime: Long = 0,
    val realSuperChapterId: Int = 0,
    val selfVisible: Int = 0,
    val shareDate: Long = 0,
    val shareUser: String? = "",
    val superChapterId: Int = 0,
    val superChapterName: String? = "",
    val tags: List<Tag> = listOf(),
    val title: String? = "",
    val type: Int = 0,
    val userId: Int = 0,
    val visible: Int = 0,
    val zan: Int = 0,
    val originId: Int? = -1,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        TODO("tags"),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (adminAdd) 1 else 0)
        parcel.writeString(apkLink)
        parcel.writeInt(audit)
        parcel.writeString(author)
        parcel.writeByte(if (canEdit) 1 else 0)
        parcel.writeInt(chapterId)
        parcel.writeString(chapterName)
        parcel.writeByte(if (collect) 1 else 0)
        parcel.writeInt(courseId)
        parcel.writeString(desc)
        parcel.writeString(descMd)
        parcel.writeString(envelopePic)
        parcel.writeByte(if (fresh) 1 else 0)
        parcel.writeString(host)
        parcel.writeInt(id)
        parcel.writeByte(if (isAdminAdd) 1 else 0)
        parcel.writeString(link)
        parcel.writeString(niceDate)
        parcel.writeString(niceShareDate)
        parcel.writeString(origin)
        parcel.writeString(prefix)
        parcel.writeString(projectLink)
        parcel.writeLong(publishTime)
        parcel.writeInt(realSuperChapterId)
        parcel.writeInt(selfVisible)
        parcel.writeLong(shareDate)
        parcel.writeString(shareUser)
        parcel.writeInt(superChapterId)
        parcel.writeString(superChapterName)
        parcel.writeString(title)
        parcel.writeInt(type)
        parcel.writeInt(userId)
        parcel.writeInt(visible)
        parcel.writeInt(zan)
        parcel.writeValue(originId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticleBean> {
        override fun createFromParcel(parcel: Parcel): ArticleBean {
            return ArticleBean(parcel)
        }

        override fun newArray(size: Int): Array<ArticleBean?> {
            return arrayOfNulls(size)
        }
    }


}