package com.tomcz.mvi.compose

import androidx.compose.runtime.*
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun <EV : Any, ST : Any, EF : Any, T> StateEffectProcessor<EV, ST, EF>.collectAsState(
    initialValue: T,
    mapper: suspend (ST) -> T
): State<T> = state.map(mapper).distinctUntilChanged().collectAsState(initial = initialValue)

@Composable
fun <EV : Any, ST : Any, T> StateProcessor<EV, ST>.collectAsState(
    initialValue: T,
    mapper: suspend (ST) -> T
): State<T> = state.map(mapper).distinctUntilChanged().collectAsState(initial = initialValue)
