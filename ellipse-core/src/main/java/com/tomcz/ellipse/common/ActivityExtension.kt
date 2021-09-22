package com.tomcz.ellipse.common

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onState, onEffect, viewEvents()) }

fun <EV : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, Nothing, Nothing>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
) = launch(lifecycleState) {
    consume(
        processor = processor(),
        trigger = {},
        viewEvents = viewEvents()
    )
}

fun <EV : Any, ST : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, Nothing>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
) = launch(lifecycleState) {
    consume(
        processor = processor(),
        render = onState,
        trigger = {},
        viewEvents = viewEvents()
    )
}

@JvmName("appCompatOnProcessorEffect")
fun <EV : Any, EF : Any> AppCompatActivity.onProcessor(
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

fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onState, onEffect, viewEvents()) }

fun <EV : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, Nothing, Nothing>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
) = launch(lifecycleState) {
    consume(
        processor = processor(),
        trigger = {},
        viewEvents = viewEvents()
    )
}

fun <EV : Any, ST : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> Processor<EV, ST, Nothing>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
) = launch(lifecycleState) {
    consume(
        processor = processor(),
        render = onState,
        trigger = {},
        viewEvents = viewEvents()
    )
}

@JvmName("componentOnProcessorEffect")
fun <EV : Any, EF : Any> ComponentActivity.onProcessor(
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
