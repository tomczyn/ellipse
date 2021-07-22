package com.tomcz.mvi.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.tomcz.mvi.StateProcessor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun <EV : Any, ST : Any, T> StateProcessor<EV, ST>.collectAsState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    mapper: (ST) -> T
): State<T> {
    val flow = state.map { mapper(it) }.distinctUntilChanged()
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }.collectAsState(initial = mapper(state.value))
}
