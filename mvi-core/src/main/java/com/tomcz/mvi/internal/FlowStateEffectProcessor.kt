package com.tomcz.mvi.internal

import com.tomcz.mvi.Effects
import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedDeque

internal class FlowStateEffectProcessor<in EV : Any, ST : Any, out PA : PartialState<ST>, EF : Any> constructor(
    private val scope: CoroutineScope,
    defaultViewState: ST,
    prepare: suspend (Effects<EF>) -> Flow<PA> = { emptyFlow() },
    private val eventEffects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    private val mapper: suspend (Effects<EF>, EV) -> Flow<PA>,
) : StateEffectProcessor<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(defaultViewState)

    private val replay: ConcurrentLinkedDeque<EF> = ConcurrentLinkedDeque()

    private val effects: Effects<EF> = object : Effects<EF> {
        override fun send(effect: EF) {
            if (effectSharedFlow.subscriptionCount.value != 0) {
                scope.launch { effectSharedFlow.emit(effect) }
            } else replay.add(effect)
        }
    }

    init {
        scope.launch { prepare(effects).collect { stateFlow.reduceAndSet(it) } }
        effectSharedFlow.subscriptionCount.onEach { subscriptions ->
            if (subscriptions != 0 && replay.peek() != null) while (replay.peek() != null) {
                replay.poll()?.let { effects.send(it) }
            }
        }.launchIn(scope)
    }

    override fun sendEvent(event: EV) {
        scope.launch { eventEffects(effects, event) }
        scope.launch { mapper(effects, event).collect { stateFlow.reduceAndSet(it) } }
    }
}
