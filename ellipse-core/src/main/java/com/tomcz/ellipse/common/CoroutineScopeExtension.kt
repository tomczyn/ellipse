package com.tomcz.ellipse.common

import com.tomcz.ellipse.EffectProcessor
import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.StateEffectProcessor
import com.tomcz.ellipse.StateProcessor
import com.tomcz.ellipse.internal.FlowEffectProcessor
import com.tomcz.ellipse.internal.FlowStateEffectProcessor
import com.tomcz.ellipse.internal.FlowStateProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun <EV : Any, ST : Any, PA : PartialState<ST>> CoroutineScope.stateProcessor(
    initialState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    onEvent: (suspend (EV) -> Flow<PA>)? = null
): StateProcessor<EV, ST> = FlowStateProcessor(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> CoroutineScope.stateEffectProcessor(
    initialState: ST,
    prepare: (suspend (EffectsCollector<EF>) -> Flow<PA>)? = null,
    onEvent: (suspend (EffectsCollector<EF>, EV) -> Flow<PA>)? = null,
): StateEffectProcessor<EV, ST, EF> = FlowStateEffectProcessor(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)

fun <EV : Any, EF : Any> CoroutineScope.effectProcessor(
    prepare: (suspend (EffectsCollector<EF>) -> Unit)? = null,
    onEvent: (suspend (EffectsCollector<EF>, EV) -> Unit)? = null,
): EffectProcessor<EV, EF> = FlowEffectProcessor(
    scope = this,
    prepare = prepare,
    onEvent = onEvent
)
