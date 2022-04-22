package com.tomcz.ellipse.common

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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
fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), viewEvents(), onState, onEffect) }

@FlowPreview
fun <EV : Any, EF : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, Nothing, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), viewEvents(), onEffect) }

@FlowPreview
fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), viewEvents(), onState, onEffect) }

@FlowPreview
fun <EV : Any, EF : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, Nothing, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), viewEvents(), onEffect) }

private fun AppCompatActivity.launch(
    lifecycleState: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(lifecycleState) { action() }
}

private fun ComponentActivity.launch(
    lifecycleState: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(lifecycleState) { action() }
}
