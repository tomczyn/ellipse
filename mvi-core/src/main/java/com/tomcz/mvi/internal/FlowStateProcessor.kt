package com.tomcz.mvi.internal

import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class FlowStateProcessor<in EV : Any, ST : Any, out PA : PartialState<ST>> constructor(
    private val scope: CoroutineScope,
    initialState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    private val mapper: (suspend (EV) -> Flow<PA>)? = null,
) : StateProcessor<EV, ST> {

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(initialState)

    init {
        prepare?.let {
            scope.launch { it.invoke().collect { stateFlow.reduceAndSet(it) } }
        }
    }

    override fun sendEvent(event: EV) {
        mapper?.let {
            scope.launch { it(event).collect { stateFlow.reduceAndSet(it) } }
        }
    }
}
