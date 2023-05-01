package com.tomczyn.sample.feature.common

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
abstract class BaseCoroutineTest(
    testDispatcher: TestDispatcher = StandardTestDispatcher()
) {
    @RegisterExtension
    @JvmField
    val scopeExtension: MainCoroutineScopeExtension = MainCoroutineScopeExtension(testDispatcher)

    val dispatcher: TestDispatcher
        get() = scopeExtension.dispatcher
}
