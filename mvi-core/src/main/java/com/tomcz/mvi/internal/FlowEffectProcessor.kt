package com.tomcz.mvi.internal

import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.Effects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class FlowEffectProcessor<in EV : Any, EF : Any>(
    private val scope: CoroutineScope,
    prepare: (suspend (Effects<EF>) -> Unit)? = null,
    private val mapper: suspend (Effects<EF>, EV) -> Unit,
) : EffectProcessor<EV, EF> {

    override val effect: Flow<EF>
        get() = _effect
    private val _effect: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    private val effects: Effects<EF> = object : Effects<EF> {
        override fun send(effect: EF) {
            scope.launch { _effect.emit(effect) }
        }
    }

    init {
        prepare?.let { scope.launch { it(effects) } }
    }

    override fun sendEvent(event: EV) {
        scope.launch { mapper(effects, event) }
    }
}
