package com.tomczyn.ellipse.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
@Suppress("UnnecessaryAbstractClass")
abstract class BaseCoroutineTest(
    testDispatcher: TestDispatcher = StandardTestDispatcher()
) {
    @RegisterExtension
    @JvmField
    val scopeExtension: MainCoroutineScopeExtension = MainCoroutineScopeExtension(testDispatcher)

    val dispatcher: TestDispatcher = scopeExtension.dispatcher
}
