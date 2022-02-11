package com.tomcz.ellipse.common

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.tomcz.ellipse.Processor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

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

@SuppressLint("ComposableNaming")
@Composable
fun <EV : Any, ST : Any, EF : Any> Processor<EV, ST, EF>.collectEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    collect: suspend (EF) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flow = remember(effect, lifecycleOwner) {
        effect.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }
    LaunchedEffect(flow) { flow.collect { collect(it) } }
}

fun <EV : Any, ST : Any, EF : Any> previewProcessor(
    state: ST
): Processor<EV, ST, EF> = object : Processor<EV, ST, EF> {

    override val state: StateFlow<ST> = MutableStateFlow(state)
    override val effect: Flow<EF> = emptyFlow()

    override fun sendEvent(vararg event: EV) {
        /* no-op */
    }
}
