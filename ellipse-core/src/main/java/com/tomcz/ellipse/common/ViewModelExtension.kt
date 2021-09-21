package com.tomcz.ellipse.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.StateEffectProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

fun <EV : Any, ST : Any, PA : PartialState<ST>> ViewModel.processor(
        initialState: ST,
        prepare: suspend () -> Flow<PA> = { emptyFlow() },
        onEvent: suspend (EV) -> Flow<PA> = { emptyFlow() },
): StateEffectProcessor<EV, ST, Nothing> = viewModelScope.processor(
        initialState = initialState,
        prepare = { prepare() },
        onEvent = { _, event -> onEvent(event) }
)

fun <EV : Any, EF : Any> ViewModel.processor(
        prepare: suspend () -> Unit = {},
        onEvent: suspend (EV) -> Unit = {},
): StateEffectProcessor<EV, Nothing, EF> = viewModelScope.processor(
        prepare = { prepare() },
        onEvent = { _, event -> onEvent(event) }
)

fun <EV : Any, ST : Any, PA : PartialState<ST>, EF : Any> ViewModel.processor(
        initialState: ST,
        prepare: suspend (EffectsCollector<EF>) -> Flow<PA> = { emptyFlow() },
        onEvent: suspend (EffectsCollector<EF>, EV) -> Flow<PA> = { _, _ -> emptyFlow() },
): StateEffectProcessor<EV, ST, EF> = viewModelScope.processor(
        initialState = initialState,
        prepare = prepare,
        onEvent = onEvent
)
