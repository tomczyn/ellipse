package com.tomcz.sample.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
abstract class BaseCoroutineTest(
    @RegisterExtension
    @JvmField
    val scopeExtension: MainCoroutineScopeExtension = MainCoroutineScopeExtension()
) : TestCoroutineScope by scopeExtension
