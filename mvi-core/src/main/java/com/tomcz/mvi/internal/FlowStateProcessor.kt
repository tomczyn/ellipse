package com.tomcz.mvi.internal

import com.tomcz.mvi.Intent
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class FlowStateProcessor<in EV : Any, ST : Any, out PA : Intent<ST>>(
    private val scope: CoroutineScope,
    defaultViewState: ST,
    prepare: suspend () -> Flow<PA> = { emptyFlow() },
    private val mapper: suspend (EV) -> Flow<PA>,
) : StateProcessor<EV, ST> {

    override val state: StateFlow<ST>
        get() = _state
    private val _state: MutableStateFlow<ST> = MutableStateFlow(defaultViewState)

    init {
        scope.launch { prepare().collect { _state.reduceAndSet(it) } }
    }

    override fun sendEvent(event: EV) {
        scope.launch { mapper(event).collect { _state.reduceAndSet(it) } }
    }
}
