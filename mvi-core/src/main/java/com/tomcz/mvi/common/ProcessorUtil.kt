package com.tomcz.mvi.common

import com.tomcz.mvi.*
import com.tomcz.mvi.internal.FlowEffectProcessor
import com.tomcz.mvi.internal.FlowStateEffectProcessor
import com.tomcz.mvi.internal.FlowStateProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, ST : Any, PA : PartialState<ST>> CoroutineScope.stateProcessor(
    defViewState: ST,
    prepare: suspend () -> Flow<PA> = { emptyFlow() },
    states: suspend (EV) -> Flow<PA> = { _ -> emptyFlow() }
): StateProcessor<EV, ST> = FlowStateProcessor(this, defViewState, prepare, states)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> CoroutineScope.stateEffectProcessor(
    defViewState: ST,
    prepare: suspend (Effects<EF>) -> Flow<PA> = { emptyFlow() },
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    statesEffects: suspend (Effects<EF>, EV) -> Flow<PA> = { _, _ -> emptyFlow() },
): StateEffectProcessor<EV, ST, EF> =
    FlowStateEffectProcessor(this, defViewState, prepare, effects, statesEffects)

fun <EV : Any, EF : Any> CoroutineScope.effectProcessor(
    prepare: suspend (Effects<EF>) -> Unit = {},
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
): EffectProcessor<EV, EF> = FlowEffectProcessor(this, prepare, effects)
