package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EllipseContext
import com.tomcz.ellipse.Processor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class FlowProcessor<in EV : Any, ST : Any, EF : Any> constructor(
    private val scope: CoroutineScope,
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Unit,
    private val onEvent: suspend EllipseContext<ST, EF>.(EV) -> Unit,
) : Processor<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = effectSharedFlow
    private val effectSharedFlow: MutableSharedFlow<EF> = MutableSharedFlow(replay = 0)

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(initialState)

    private val effectCache: MutableList<EF> = mutableListOf()

    private val context: EllipseContext<ST, EF> =
        object : EllipseContext<ST, EF>, CoroutineScope by scope {

            override var state: ST
                get() = stateFlow.value
                set(value) {
                    stateFlow.value = value
                }

            override fun sendEffect(vararg effect: EF) {
                scope.launch {
                    this@FlowProcessor.sendEffect(*effect)
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

    private suspend fun sendEffect(vararg effect: EF) {
        effect.forEach {
            if (effectSharedFlow.subscriptionCount.value != 0) {
                effectSharedFlow.emit(it)
            } else {
                effectCache.add(it)
            }
        }
    }
}
