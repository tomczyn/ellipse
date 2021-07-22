package com.tomcz.mvi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun <EV : Any, ST : Any, EF : Any, T> StateEffectProcessor<EV, ST, EF>.collectAsState(
    mapper: (ST) -> T
): State<T> =
    state.map { mapper(it) }.distinctUntilChanged().collectAsState(initial = mapper(state.value))

@Composable
fun <EV : Any, ST : Any, T> StateProcessor<EV, ST>.collectAsState(
    mapper: (ST) -> T
): State<T> =
    state.map { mapper(it) }.distinctUntilChanged().collectAsState(initial = mapper(state.value))
