package com.tomcz.ellipse.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.tomcz.ellipse.Processor
import kotlinx.coroutines.flow.*

@Composable
fun <EV : Any, ST : Any, T> Processor<EV, ST, *>.collectAsState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    mapper: (ST) -> T
): State<T> {
    val flow = state.map { mapper(it) }.distinctUntilChanged()
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }.collectAsState(initial = mapper(state.value))
}

fun <EV : Any, ST : Any, EF : Any> previewProcessor(
    state: ST
): Processor<EV, ST, EF> = object : Processor<EV, ST, EF> {
    override val state: StateFlow<ST> = MutableStateFlow(state)
    override fun sendEvent(event: EV) {}
    override val effect: Flow<EF> = emptyFlow()
}
