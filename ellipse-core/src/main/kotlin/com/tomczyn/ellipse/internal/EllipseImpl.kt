package com.tomczyn.ellipse.internal

import com.tomczyn.ellipse.EffectsCollector
import com.tomczyn.ellipse.PartialState
import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.common.EllipseContext
import com.tomczyn.ellipse.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class EllipseImpl<in EV : Any, ST : Any, out PA : PartialState<ST>, EF : Any> constructor(
    private val scope: CoroutineScope,
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA>,
    private val onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA>,
) : Ellipse<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(initialState)

    private val effectCache: MutableList<EF> = mutableListOf()

    private val context: EllipseContext<ST, EF>
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
        scope.launch {
            prepare(context).collect { stateFlow.reduceAndSet(it) }
        }
    }

    override fun sendEvent(vararg event: EV) {
        scope.launch {
            event.forEach {
                onEvent(context, it).collect { partial -> stateFlow.reduceAndSet(partial) }
            }
        }
    }
}
