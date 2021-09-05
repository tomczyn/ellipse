package com.tomcz.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.ellipse.EffectProcessor
import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.StateEffectProcessor
import com.tomcz.ellipse.StateProcessor
import kotlinx.coroutines.flow.Flow

fun <EV : Any, ST : Any, PA : PartialState<ST>> ViewModel.stateProcessor(
    initialState: ST,
    prepare: (suspend () -> Flow<PA>)? = null,
    onEvent: (suspend (EV) -> Flow<PA>)? = null
): StateProcessor<EV, ST> = viewModelScope.stateProcessor(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.stateEffectProcessor(
    initialState: ST,
    prepare: (suspend (EffectsCollector<EF>) -> Flow<PA>)? = null,
    onEvent: (suspend (EffectsCollector<EF>, EV) -> Flow<PA>)? = null,
): StateEffectProcessor<EV, ST, EF> = viewModelScope.stateEffectProcessor(
    initialState = initialState,
    prepare = prepare,
    onEvent = onEvent
)

fun <EV : Any, EF : Any> ViewModel.effectProcessor(
    prepare: (suspend (EffectsCollector<EF>) -> Unit)? = null,
    onEvent: (suspend (EffectsCollector<EF>, EV) -> Unit)? = null,
): EffectProcessor<EV, EF> = viewModelScope.effectProcessor(
    prepare = prepare,
    onEvent = onEvent
)
