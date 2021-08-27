package com.tomcz.mvi.common

import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.EffectsCollector
import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.FlowEffectProcessor
import com.tomcz.mvi.internal.FlowStateEffectProcessor
import com.tomcz.mvi.internal.FlowStateProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun <EV : Any, ST : Any, PA : PartialState<ST>> CoroutineScope.stateProcessor(
    initialState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    states: (suspend (EV) -> Flow<PA>)? = null
): StateProcessor<EV, ST> = FlowStateProcessor(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    mapper = states
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> CoroutineScope.stateEffectProcessor(
    initialState: ST,
    prepare: (suspend (EffectsCollector<EF>) -> Flow<PA>)? = null,
    statesEffects: (suspend (EffectsCollector<EF>, EV) -> Flow<PA>)? = null,
): StateEffectProcessor<EV, ST, EF> = FlowStateEffectProcessor(
    scope = this,
    initialState = initialState,
    prepare = prepare,
    mapper = statesEffects
)

fun <EV : Any, EF : Any> CoroutineScope.effectProcessor(
    prepare: (suspend (EffectsCollector<EF>) -> Unit)? = null,
    effects: (suspend (EffectsCollector<EF>, EV) -> Unit)? = null,
): EffectProcessor<EV, EF> = FlowEffectProcessor(
    scope = this,
    prepare = prepare,
    mapper = effects
)
