package com.pp.network.cookie

import okhttp3.Cookie

interface ICookieCache {

    fun snapshot(): Collection<Cookie>

    fun saveAll(cookies: Collection<Cookie>)

    fun removeAll(cookies: Collection<Cookie>)

    fun clear()

}