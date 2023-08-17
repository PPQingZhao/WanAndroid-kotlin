package com.pp.common.http.wanandroid.bean

/*

{
	"data": {
		"curPage": 1,
		"datas": [
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
		],
		"offset": 0,
		"over": false,
		"pageCount": 727,
		"size": 20,
		"total": 14530
	},
	"errorCode": 0,
	"errorMsg": ""
}


*/
data class PageBean(
    val curPage: Int = 0,
    val datas: List<ArticleBean> = listOf(),
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0,
)
