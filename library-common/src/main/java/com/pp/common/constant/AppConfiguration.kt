package com.pp.common.constant

import androidx.datastore.preferences.core.stringPreferencesKey


// 转场动画时长
const val TRANSITION_DURATION = 500L

const val ON_BACK_PRESSED = "on back pressed"

const val MAX_COUNT_SEARCH_HOTKEY_HISTORY = 10
const val KEY_SAVE_SEARCH_HOTKEY_HISTORY = ";"


const val PREFERENCES_KEY_USER_NAME = "user_name"
const val PREFERENCES_KEY_SEARCH_HOTKEY_HISTORY = "hotkey_history"

val preferences_key_user_name by lazy { stringPreferencesKey(PREFERENCES_KEY_USER_NAME) }
val preferences_key_search_hotkey_history by lazy {
    stringPreferencesKey(
        PREFERENCES_KEY_SEARCH_HOTKEY_HISTORY
    )
}