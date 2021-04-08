package com.tomcz.mvi.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.mvi.*
import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.Effects
import com.tomcz.mvi.Intent
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.internal.FlowEffectProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, ST : Any, PA : Intent<ST>> ViewModel.stateProcessor(
    defViewState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    states: suspend (EV) -> Flow<PA> = { _ -> emptyFlow() }
): StateProcessor<EV, ST> = viewModelScope.stateProcessor(defViewState, prepare, states)

fun <EV : Any, ST : Any, PA : Intent<ST>, EF : Any> ViewModel.stateEffectProcessor(
    defViewState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    prepareEffects: (suspend (Effects<EF>) -> Unit)? = null,
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
    statesEffects: suspend (Effects<EF>, EV) -> Flow<PA> = { _, _ -> emptyFlow() }
): StateEffectProcessor<EV, ST, EF> =
    viewModelScope.stateEffectProcessor(
        defViewState,
        prepare,
        prepareEffects,
        effects,
        statesEffects
    )

fun <EV : Any, EF : Any> ViewModel.effectProcessor(
    prepare: (suspend (Effects<EF>) -> Unit)? = null,
    effects: suspend (Effects<EF>, EV) -> Unit = { _, _ -> },
): EffectProcessor<EV, EF> = FlowEffectProcessor(viewModelScope, prepare, effects)
