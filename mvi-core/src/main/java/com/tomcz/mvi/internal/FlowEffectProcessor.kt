package com.tomcz.mvi.internal

import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.EffectsCollector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

internal class FlowEffectProcessor<in EV : Any, EF : Any>(
    private val scope: CoroutineScope,
    prepare: (suspend (EffectsCollector<EF>) -> Unit)? = null,
    private val onEvent: (suspend (EffectsCollector<EF>, EV) -> Unit)? = null,
) : EffectProcessor<EV, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    private val effectsCollector: EffectsCollector<EF> = object : EffectsCollector<EF> {
        override fun send(effect: EF) {
            scope.launch { effectSharedFlow.emit(effect) }
        }
    }

    init {
        prepare?.let {
            scope.launch { it(effectsCollector) }
        }
    }

    override fun sendEvent(event: EV) {
        onEvent?.let {
            scope.launch { it(effectsCollector, event) }
        }
    }
}
