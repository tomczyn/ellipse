package com.tomczyn.ellipse.common

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.tomczyn.ellipse.Ellipse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

@Composable
fun <EV : Any, ST : Any, T> Ellipse<EV, ST, *>.collectAsState(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    mapper: (ST) -> T
): State<T> {
    val initial: T = remember { mapper(state.value) }
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember(state, lifecycleOwner) {
        state.map { mapper(it) }.distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }.collectAsState(initial = initial)
}

@SuppressLint("ComposableNaming")
@Composable
fun <EV : Any, ST : Any, EF : Any> Ellipse<EV, ST, EF>.collectEffect(
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    collect: suspend (EF) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flow = remember(effect, lifecycleOwner) {
        effect.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }
    LaunchedEffect(flow) { flow.collect { collect(it) } }
}

fun <EV : Any, ST : Any, EF : Any> previewEllipse(
    state: ST
): Ellipse<EV, ST, EF> = object : Ellipse<EV, ST, EF> {

    override val state: StateFlow<ST> = MutableStateFlow(state)
    override val effect: Flow<EF> = emptyFlow()

    override fun sendEvent(vararg event: EV) {
        /* no-op */
    }
}
