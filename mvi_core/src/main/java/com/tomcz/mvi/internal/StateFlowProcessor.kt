package com.tomcz.mvi.internal

import com.tomcz.mvi.Intent
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class StateFlowProcessor<in EV : Any, ST : Any, out PA : Intent<ST>>(
    private val scope: CoroutineScope,
    defaultViewState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    private val mapper: suspend (EV) -> Flow<PA>,
) : StateProcessor<EV, ST> {

    override val state: StateFlow<ST>
        get() = _state
    private val _state: MutableStateFlow<ST> = MutableStateFlow(defaultViewState)

    init {
        prepare?.let { flowWrapper -> scope.launch { flowWrapper().collect { _state.reduceAndSet(it) } } }
    }

    override fun process(event: EV) {
        scope.launch { mapper(event).collect { _state.reduceAndSet(it) } }
    }
}
