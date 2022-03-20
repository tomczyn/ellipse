package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.EllipseContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class FlowEffectProcessor<in EV : Any, EF : Any> constructor(
    private val scope: CoroutineScope,
    prepare: suspend EllipseContext<Nothing, EF>.() -> Unit,
    private val onEvent: suspend EllipseContext<Nothing, EF>.(EV) -> Unit
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
            override suspend fun collect(collector: FlowCollector<Nothing>): Nothing {
                throw IllegalStateException()
            }
        }

    private val effectCache: MutableList<EF> = mutableListOf()

    private val context: EllipseContext<Nothing, EF>
        get() = EllipseContext(state, effectsCollector)

    private val effectsCollector: EffectsCollector<EF> = object : EffectsCollector<EF> {
        override fun send(vararg effect: EF) {
            scope.launch {
                effect.forEach {
                    if (effectSharedFlow.subscriptionCount.value != 0) {
                        effectSharedFlow.emit(it)
                    } else {
                        effectCache.add(it)
                    }
                }
            }
        }
    }

    init {
        scope.launch {
            effectSharedFlow.subscriptionCount.collect { subscribers ->
                if (subscribers != 0) {
                    while (effectCache.isNotEmpty()) {
                        effectSharedFlow.emit(effectCache.removeFirst())
                    }
                }
            }
        }
        scope.launch { prepare(context) }
    }

    override fun sendEvent(vararg event: EV) {
        scope.launch { event.forEach { onEvent(context, it) } }
    }
}
