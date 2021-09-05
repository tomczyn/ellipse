package com.tomcz.ellipse.internal

import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.StateProcessor
import com.tomcz.ellipse.internal.util.reduceAndSet
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
    private val onEvent: (suspend (EV) -> Flow<PA>)? = null,
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
        onEvent?.let {
            scope.launch { it(event).collect { stateFlow.reduceAndSet(it) } }
        }
    }
}
