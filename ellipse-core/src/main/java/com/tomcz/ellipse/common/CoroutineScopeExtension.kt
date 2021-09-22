package com.tomcz.ellipse.common


import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.FlowEffectProcessor
import com.tomcz.ellipse.internal.FlowStateEffectProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, EF : Any> CoroutineScope.processor(
    prepare: suspend EffectsCollector<EF>.() -> Unit = {},
    onEvent: suspend EffectsCollector<EF>.(EV) -> Unit = {},
): Processor<EV, Nothing, EF> = FlowEffectProcessor(
    scope = this,
    prepare = prepare,
    onEvent = onEvent
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> CoroutineScope.processor(
    initialState: ST,
    prepare: suspend EffectsCollector<EF>.() -> Flow<PA> = { emptyFlow() },
    onEvent: suspend EffectsCollector<EF>.(EV) -> Flow<PA> = { emptyFlow() },
): Processor<EV, ST, EF> = FlowStateEffectProcessor(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)
