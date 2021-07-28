package com.tomcz.mvi.internal

import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class FlowStateProcessor<in EV : Any, ST : Any, out PA : PartialState<ST>>(
    private val scope: CoroutineScope,
    initialState: ST,
    prepare: suspend () -> Flow<PA> = { emptyFlow() },
    private val mapper: suspend (EV) -> Flow<PA>,
) : StateProcessor<EV, ST> {

    override val state: StateFlow<ST>
        get() = _state
    private val _state: MutableStateFlow<ST> = MutableStateFlow(initialState)

    init {
        scope.launch { prepare().collect { _state.reduceAndSet(it) } }
    }

    override fun sendEvent(event: EV) {
        scope.launch { mapper(event).collect { _state.reduceAndSet(it) } }
    }
}
