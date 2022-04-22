package com.tomcz.ellipse.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@FlowPreview
fun <EV : Any, ST : Any, EF : Any> Fragment.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) {
    consume(
        processor = processor(),
        render = onState,
        trigger = onEffect,
        viewEvents = viewEvents()
    )
}

@FlowPreview
fun <EV : Any, EF : Any> Fragment.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, Nothing, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) {
    consume(
        processor = processor(),
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
