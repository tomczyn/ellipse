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

@FlowPreview
fun <EV : Any, ST : Any, EF : Any> Fragment.onProcessor(
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
