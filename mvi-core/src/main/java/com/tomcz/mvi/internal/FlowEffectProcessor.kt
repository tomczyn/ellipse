package com.tomcz.mvi.internal

import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.Effects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedDeque

internal class FlowEffectProcessor<in EV : Any, EF : Any>(
    private val scope: CoroutineScope,
    prepare: suspend (Effects<EF>) -> Unit = {},
    private val mapper: suspend (Effects<EF>, EV) -> Unit,
) : EffectProcessor<EV, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    private val replay: ConcurrentLinkedDeque<EF> = ConcurrentLinkedDeque()

    private val effects: Effects<EF> = object : Effects<EF> {
        override fun send(effect: EF) {
            if (effectSharedFlow.subscriptionCount.value != 0) {
                scope.launch { effectSharedFlow.emit(effect) }
            } else replay.add(effect)
        }
    }

    init {
        scope.launch { prepare(effects) }
        effectSharedFlow.subscriptionCount.onEach { subscriptions ->
            if (subscriptions != 0 && replay.peek() != null) while (replay.peek() != null) {
                replay.poll()?.let { effects.send(it) }
            }
        }.launchIn(scope)
    }

    override fun sendEvent(event: EV) {
        scope.launch { mapper(effects, event) }
    }
}
