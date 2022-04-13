package com.tomcz.ellipse.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler

@OptIn(ExperimentalCoroutinesApi::class)
data class TestResultContext<T : Any>(
    private val testResult: TestResult<T>,
    val testScheduler: TestCoroutineScheduler
) : TestResult<T> by testResult
