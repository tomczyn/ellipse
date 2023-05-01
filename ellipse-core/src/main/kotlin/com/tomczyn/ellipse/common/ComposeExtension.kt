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

/**
 * A composable function to collect the state from an [Ellipse] model and map it to another type [T].
 *
 * @param lifecycleState The [Lifecycle.State] in which the coroutine should run.
 * @param mapper A lambda that maps the state of type [ST] to another type [T].
 */
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

/**
 * A composable function to collect effects from an [Ellipse] model and handle them.
 *
 * @param lifecycleState The [Lifecycle.State] in which the coroutine should run.
 * @param collect A suspend lambda that handles the effects emitted by the Ellipse model.
 */
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

/**
* Creates a previewEllipse instance which is a lightweight version of an Ellipse with no side effects.
* This function is useful for creating preview instances of an Ellipse with a given initial state,
* without having to provide the actual implementation of the sendEvent function.
* It can be used for testing or previewing the behavior of an Ellipse in a UI framework.
* @param ST The type of the state in the Ellipse.
* @param EV The type of the event in the Ellipse.
* @param EF The type of the effect in the Ellipse.
* @param state The initial state of the Ellipse.
* @return An instance of Ellipse with the given initial state, and with no side effects.
* @see Ellipse
* @see StateFlow
* @see MutableStateFlow
* @see Flow
* @see emptyFlow
*/
fun <EV : Any, ST : Any, EF : Any> previewEllipse(
    state: ST
): Ellipse<EV, ST, EF> = object : Ellipse<EV, ST, EF> {

    override val state: StateFlow<ST> = MutableStateFlow(state)
    override val effect: Flow<EF> = emptyFlow()

    override fun sendEvent(vararg event: EV) {
        /* no-op */
    }
}
