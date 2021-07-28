package com.tomcz.mvi.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.Effects
import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.FlowEffectProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, ST : Any, PA : PartialState<ST>> ViewModel.stateProcessor(
    initialState: ST,
    prepare: suspend () -> Flow<PA> = { emptyFlow() },
    states: suspend (EV) -> Flow<PA> = { emptyFlow() }
): StateProcessor<EV, ST> = viewModelScope.stateProcessor(
    initialState = initialState,
    prepare = prepare,
    states = states
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.stateEffectProcessor(
    initialState: ST,
    prepare: suspend (Effects<EF>) -> Flow<PA> = { emptyFlow() },
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    statesEffects: suspend (Effects<EF>, EV) -> Flow<PA> = { _, _ -> emptyFlow() }
): StateEffectProcessor<EV, ST, EF> = viewModelScope.stateEffectProcessor(
    initialState = initialState,
    prepare = prepare,
    effects = effects,
    statesEffects = statesEffects
)

fun <EV : Any, EF : Any> ViewModel.effectProcessor(
    prepare: suspend (Effects<EF>) -> Unit = {},
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
): EffectProcessor<EV, EF> = FlowEffectProcessor(
    scope = viewModelScope,
    prepare = prepare,
    mapper = effects
)
