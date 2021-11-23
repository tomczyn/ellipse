package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class FlowProcessor<in EV : Any, ST : Any, out PA : PartialState<ST>, EF : Any> constructor(
    private val scope: CoroutineScope,
    initialState: ST,
    prepare: suspend (EffectsCollector<EF>) -> Flow<PA>,
    private val onEvent: suspend (EffectsCollector<EF>, EV) -> Flow<PA>,
) : Processor<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(initialState)

    private val effectsCollector: EffectsCollector<EF> = object : EffectsCollector<EF> {
        override fun sendEffect(effect: EF) {
            scope.launch { effectSharedFlow.emit(effect) }
        }
    }

    init {
        scope.launch {
            prepare(effectsCollector).collect { stateFlow.reduceAndSet(it) }
        }
    }

    override fun sendEvent(event: EV) {
        scope.launch { onEvent(effectsCollector, event).collect { stateFlow.reduceAndSet(it) } }
    }
}
