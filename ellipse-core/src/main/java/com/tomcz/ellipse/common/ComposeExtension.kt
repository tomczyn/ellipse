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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

@Composable
fun <EV : Any, ST : Any, T> Processor<EV, ST, *>.collectAsState(
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
inline fun <EV : Any, ST : Any, reified EF : Any, reified T : EF> Processor<EV, ST, EF>.collectEffect(
    effectClass: KClass<T>,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline collect: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val filteredFlow = remember(this, effectClass) { effect(effectClass) }
    val flow = remember(filteredFlow, lifecycleOwner) {
        filteredFlow.flowWithLifecycle(lifecycleOwner.lifecycle, lifecycleState)
    }
    LaunchedEffect(flow) { flow.collect { collect(it) } }
}

fun <EV : Any, ST : Any, EF : Any> previewProcessor(
    state: ST
): Processor<EV, ST, EF> = object : Processor<EV, ST, EF> {

    override val state: StateFlow<ST> = MutableStateFlow(state)
    override val effect: Flow<EF> = emptyFlow()
    override fun <T : EF> effect(filterClass: KClass<T>): Flow<T> = emptyFlow()

    override fun sendEvent(vararg event: EV) {
        /* no-op */
    }
}
