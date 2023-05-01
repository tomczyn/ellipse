package com.tomczyn.sample.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {

    val main: CoroutineDispatcher
        get() = Dispatchers.Main

    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    val unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    val default: CoroutineDispatcher
        get() = Dispatchers.Default
}

object AndroidDispatcherProvider : DispatcherProvider
