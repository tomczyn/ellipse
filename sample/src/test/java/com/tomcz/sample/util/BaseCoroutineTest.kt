package com.tomcz.sample.util

import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.jupiter.api.extension.RegisterExtension

abstract class BaseCoroutineTest(
    @RegisterExtension
    @JvmField
    val scopeExtension: MainCoroutineScopeExtension = MainCoroutineScopeExtension()
) : TestCoroutineScope by scopeExtension
