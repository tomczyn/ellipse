package com.tomczyn.ellipse.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Launches a coroutine to consume the given `Ellipse` instance, render its states, and trigger its effects within a [Fragment].
 *
 * @param EV The type of the event in the `Ellipse`.
 * @param ST The type of the state in the `Ellipse`.
 * @param EF The type of the effect in the `Ellipse`.
 * @param lifecycleState The [Lifecycle.State] at which the coroutine should run.
 * @param ellipse A function that returns an instance of `Ellipse` to be consumed.
 * @param viewEvents A function that returns a list of event flows to be sent to the `Ellipse`. Default is an empty list.
 * @param onState A function that is called with the current state of the `Ellipse` each time it changes. Default is an empty lambda.
 * @param onEffect A function that is called with the current effect of the `Ellipse` each time it's emitted. Default is an empty lambda.
 */
@FlowPreview
fun <EV : Any, ST : Any, EF : Any> Fragment.onEllipse(
    lifecycleState: Lifecycle.State,
    ellipse: () -> Ellipse<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) {
    consume(
        ellipse = ellipse(),
        render = onState,
        trigger = onEffect,
        viewEvents = viewEvents()
    )
}

private fun Fragment.launch(
    lifecycleState: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
): Job = viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(lifecycleState) { action() }
}
