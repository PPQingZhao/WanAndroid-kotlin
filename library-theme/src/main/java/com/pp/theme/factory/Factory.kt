package com.pp.theme.factory

/**
 * 工厂
 */
interface Factory<T> {

    @Throws(RuntimeException::class)
    fun create(): T
}