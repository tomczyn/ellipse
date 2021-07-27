package com.tomcz.mvi.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.mvi.*
import com.tomcz.mvi.internal.FlowEffectProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, ST : Any, PA : PartialState<ST>> ViewModel.stateProcessor(
    defViewState: ST,
    prepare: suspend () -> Flow<PA> = { emptyFlow() },
    states: suspend (EV) -> Flow<PA> = { emptyFlow() }
): StateProcessor<EV, ST> = viewModelScope.stateProcessor(defViewState, prepare, states)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.stateEffectProcessor(
    defViewState: ST,
    prepare: suspend (Effects<EF>) -> Flow<PA> = { emptyFlow() },
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    statesEffects: suspend (Effects<EF>, EV) -> Flow<PA> = { _, _ -> emptyFlow() }
): StateEffectProcessor<EV, ST, EF> = viewModelScope.stateEffectProcessor(
    defViewState,
    prepare,
    effects,
    statesEffects
)

fun <EV : Any, EF : Any> ViewModel.effectProcessor(
    prepare: suspend (Effects<EF>) -> Unit = { },
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
): EffectProcessor<EV, EF> = FlowEffectProcessor(viewModelScope, prepare, effects)
