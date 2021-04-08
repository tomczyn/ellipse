package com.tomcz.mvi.common

import com.tomcz.mvi.*
import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.Effects
import com.tomcz.mvi.Intent
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.internal.FlowEffectProcessor
import com.tomcz.mvi.internal.FlowStateEffectProcessor
import com.tomcz.mvi.internal.StateFlowProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, ST : Any, PA : Intent<ST>> CoroutineScope.stateProcessor(
    defViewState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    states: suspend (EV) -> Flow<PA> = { _ -> emptyFlow() }
): StateProcessor<EV, ST> = StateFlowProcessor(this, defViewState, prepare, states)

fun <EV : Any, ST : Any, PA : Intent<ST>, EF : Any> CoroutineScope.stateEffectProcessor(
    defViewState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    prepareEffect: (suspend (Effects<EF>) -> Unit)? = null,
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    statesEffects: suspend (Effects<EF>, EV) -> Flow<PA> = { _, _ -> emptyFlow() },
): StateEffectProcessor<EV, ST, EF> =
    FlowStateEffectProcessor(this, defViewState, prepare, prepareEffect, effects, statesEffects)

fun <EV : Any, EF : Any> CoroutineScope.effectProcessor(
    prepare: (suspend (Effects<EF>) -> Unit)? = null,
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
): EffectProcessor<EV, EF> = FlowEffectProcessor(this, prepare, effects)
