package com.tomcz.ellipse.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.tomcz.ellipse.StateEffectProcessor
import com.tomcz.ellipse.StateProcessor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
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

fun <EV : Any, ST : Any> previewStateProcessor(
    state: ST
): StateProcessor<EV, ST> = object : StateProcessor<EV, ST> {
    override val state: StateFlow<ST> = MutableStateFlow(state)
    override fun sendEvent(event: EV) {}
}

fun <EV : Any, ST : Any, EF : Any> previewStateEffectProcessor(
    state: ST
): StateEffectProcessor<EV, ST, EF> = object : StateEffectProcessor<EV, ST, EF> {
    override val state: StateFlow<ST> = MutableStateFlow(state)
    override fun sendEvent(event: EV) {}
    override val effect: Flow<EF> = emptyFlow()
}
