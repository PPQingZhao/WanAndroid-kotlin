package com.pp.common.constant

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey


// 转场动画时长
const val TRANSITION_DURATION = 500L

// 搜索记录缓存最大数量
const val MAX_COUNT_SEARCH_HOTKEY_HISTORY = 10

// 搜索记录缓存分隔符
const val KEY_SAVE_SEARCH_HOTKEY_HISTORY = ";"

private const val PREFERENCES_KEY_USER_NAME = "user_name"
private const val PREFERENCES_KEY_SEARCH_HOTKEY_HISTORY = "hotkey_history_"

/**
 * 用户名缓存key
 */
val preferences_key_user_name by lazy { stringPreferencesKey(PREFERENCES_KEY_USER_NAME) }

/**
 * 搜索记录缓存key
 */
fun preferences_key_search_hotkey_history(userId: Long?): Preferences.Key<String> {
    return stringPreferencesKey(
        PREFERENCES_KEY_SEARCH_HOTKEY_HISTORY + userId
    )
}