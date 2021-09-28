package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.Processor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class FlowEffectProcessor<in EV : Any, EF : Any> constructor(
    private val scope: CoroutineScope,
    prepare: suspend (EffectsCollector<EF>) -> Unit,
    private val onEvent: suspend (EffectsCollector<EF>, EV) -> Unit
) : Processor<EV, Nothing, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<Nothing> =
        object : StateFlow<Nothing> {
            override val replayCache: List<Nothing>
                get() = emptyList()
            override val value: Nothing
                get() = throw IllegalStateException("Can't access Nothing value")

            @OptIn(InternalCoroutinesApi::class)
            @InternalCoroutinesApi
            override suspend fun collect(collector: FlowCollector<Nothing>) {
                /* Nothing */
            }
        }

    private val effectsCollector: EffectsCollector<EF> = object : EffectsCollector<EF> {
        override fun sendEffect(effect: EF) {
            scope.launch { effectSharedFlow.emit(effect) }
        }
    }

    init {
        scope.launch { prepare(effectsCollector) }
    }

    override fun sendEvent(event: EV) {
        scope.launch { onEvent(effectsCollector, event) }
    }
}
