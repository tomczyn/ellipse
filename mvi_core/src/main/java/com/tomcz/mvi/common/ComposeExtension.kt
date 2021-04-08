package com.tomcz.mvi.common

import androidx.compose.runtime.*
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun <EV : Any, ST : Any, EF : Any, T> StateEffectProcessor<EV, ST, EF>.collectState(
    initialValue: T,
    mapper: suspend (ST) -> T
): State<T> = state.map(mapper).distinctUntilChanged().collectAsState(initial = initialValue)

@Composable
fun <EV : Any, ST : Any, T> StateProcessor<EV, ST>.collectState(
    initialValue: T,
    mapper: suspend (ST) -> T
): State<T> = state.map(mapper).distinctUntilChanged().collectAsState(initial = initialValue)
