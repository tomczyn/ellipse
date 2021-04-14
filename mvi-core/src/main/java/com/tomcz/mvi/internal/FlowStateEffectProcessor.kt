package com.tomcz.mvi.internal

import com.tomcz.mvi.Effects
import com.tomcz.mvi.Intent
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class FlowStateEffectProcessor<in EV : Any, ST : Any, out PA : Intent<ST>, EF : Any> constructor(
    private val scope: CoroutineScope,
    defaultViewState: ST,
    prepare: (suspend (Effects<EF>) -> Flow<PA>)? = null,
    private val eventEffects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    private val mapper: suspend (Effects<EF>, EV) -> Flow<PA>,
) : StateEffectProcessor<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = _effect
    private val _effect: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<ST>
        get() = _state
    private val _state: MutableStateFlow<ST> = MutableStateFlow(defaultViewState)

    private val effects: Effects<EF> = object : Effects<EF> {
        override fun send(effect: EF) {
            scope.launch { _effect.emit(effect) }
        }
    }

    init {
        if (prepare != null) scope.launch {
            prepare(effects).collect { _state.reduceAndSet(it) }
        }
    }

    override fun sendEvent(event: EV) {
        scope.launch { eventEffects(effects, event) }
        scope.launch { mapper(effects, event).collect { _state.reduceAndSet(it) } }
    }
}
