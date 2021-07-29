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
import java.util.Deque
import java.util.LinkedList

internal class FlowStateEffectProcessor<in EV : Any, ST : Any, out PA : PartialState<ST>, EF : Any> constructor(
    private val scope: CoroutineScope,
    initialState: ST,
    prepare: suspend (Effects<EF>) -> Flow<PA> = { emptyFlow() },
    private val eventEffects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    private val mapper: suspend (Effects<EF>, EV) -> Flow<PA>,
) : StateEffectProcessor<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(initialState)

    private val replay: Deque<EF> = LinkedList()

    private val effects: Effects<EF> = object : Effects<EF> {
        override fun send(effect: EF) {
            scope.launch {
                if (effectSharedFlow.subscriptionCount.value != 0) {
                    effectSharedFlow.emit(effect)
                } else replay.add(effect)
            }
        }
    }

    init {
        effectSharedFlow.subscriptionCount.onEach { subscriptions ->
            if (subscriptions != 0) while (replay.peek() != null) {
                replay.poll()?.let { effectSharedFlow.emit(it) }
            }
        }.launchIn(scope)
        scope.launch { prepare(effects).collect { stateFlow.reduceAndSet(it) } }
    }

    override fun sendEvent(event: EV) {
        scope.launch { eventEffects(effects, event) }
        scope.launch { mapper(effects, event).collect { stateFlow.reduceAndSet(it) } }
    }
}
