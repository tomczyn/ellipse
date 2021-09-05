package com.tomcz.ellipse.common

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tomcz.ellipse.EffectProcessor
import com.tomcz.ellipse.StateEffectProcessor
import com.tomcz.ellipse.StateProcessor
import com.tomcz.ellipse.internal.util.consume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@JvmName("onProcessorStateProcessor")
fun <EV : Any, ST : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> StateProcessor<EV, ST>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onState, viewEvents()) }

fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onState, onEffect, viewEvents()) }

@JvmName("onProcessorEffectProcessor")
fun <EV : Any, EF : Any> AppCompatActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> EffectProcessor<EV, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onEffect, viewEvents()) }

@JvmName("onProcessorStateProcessor")
fun <EV : Any, ST : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> StateProcessor<EV, ST>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onState, viewEvents()) }

@JvmName("onProcessorStateEffectProcessor")
fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onState, onEffect, viewEvents()) }

@JvmName("onProcessorEffectProcessor")
fun <EV : Any, EF : Any> ComponentActivity.onProcessor(
    lifecycleState: Lifecycle.State,
    processor: () -> EffectProcessor<EV, EF>,
    viewEvents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = launch(lifecycleState) { consume(processor(), onEffect, viewEvents()) }

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
