package com.tomcz.ellipse.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class FlowTester<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) {

    val values: List<T>
        get() = _values
    private val _values: MutableList<T> = mutableListOf()
    private val job: Job = scope.launch { flow.toList(_values) }

    fun finish() {
        job.cancel()
    }
}
