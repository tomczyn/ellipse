package com.tomcz.mvi.test

import com.tomcz.mvi.common.DispatcherProvider

fun BaseCoroutineTest.getDispatchers(): DispatcherProvider =
    TestDispatcherProvider(scopeExtension.dispatcher)
