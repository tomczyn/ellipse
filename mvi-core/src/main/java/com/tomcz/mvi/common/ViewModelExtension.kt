package com.tomcz.mvi.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.EffectsCollector
import com.tomcz.mvi.PartialState
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import kotlinx.coroutines.flow.Flow

fun <EV : Any, ST : Any, PA : PartialState<ST>> ViewModel.stateProcessor(
    initialState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    states: (suspend (EV) -> Flow<PA>)? = null
): StateProcessor<EV, ST> = viewModelScope.stateProcessor(
    initialState = initialState,
    prepare = prepare,
    states = states
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.stateEffectProcessor(
    initialState: ST,
    prepare: (suspend (EffectsCollector<EF>) -> Flow<PA>)? = null,
    statesEffects: (suspend (EffectsCollector<EF>, EV) -> Flow<PA>)? = null,
): StateEffectProcessor<EV, ST, EF> = viewModelScope.stateEffectProcessor(
    initialState = initialState,
    prepare = prepare,
    statesEffects = statesEffects
)

fun <EV : Any, EF : Any> ViewModel.effectProcessor(
    prepare: (suspend (EffectsCollector<EF>) -> Unit)? = null,
    effects: (suspend (EffectsCollector<EF>, EV) -> Unit)? = null,
): EffectProcessor<EV, EF> = viewModelScope.effectProcessor(
    prepare = prepare,
    effects = effects
)
