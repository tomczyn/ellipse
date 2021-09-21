package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.StateEffectProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class FlowEffectProcessor<in EV : Any, EF : Any> constructor(
        private val scope: CoroutineScope,
        prepare: (suspend (EffectsCollector<EF>) -> Unit)? = null,
        private val onEvent: (suspend (EffectsCollector<EF>, EV) -> Unit)? = null,
) : StateEffectProcessor<EV, Nothing, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<Nothing> = object : StateFlow<Nothing> {
        override val replayCache: List<Nothing>
            get() = emptyList()
        override val value: Nothing = TODO()

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Nothing>) {
        }
    }

    private val effectsCollector: EffectsCollector<EF> = object : EffectsCollector<EF> {
        override fun send(effect: EF) {
            scope.launch { effectSharedFlow.emit(effect) }
        }
    }

    init {
        prepare?.let {
            scope.launch {
                it(effectsCollector)
            }
        }
    }

    override fun sendEvent(event: EV) {
        onEvent?.let {
            scope.launch { it(effectsCollector, event) }
        }
    }

}
