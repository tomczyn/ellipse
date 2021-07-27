package com.tomcz.mvi.common

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.StateProcessor
import com.tomcz.mvi.internal.util.consume
import kotlinx.coroutines.flow.Flow

@JvmName("OnCreatedStateProcessor")
fun <EV : Any, ST : Any> AppCompatActivity.onCreated(
    processor: () -> StateProcessor<EV, ST>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, intents()) }

fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onCreated(
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, onEffect, intents()) }

@JvmName("OnCreatedEffectProcessor")
fun <EV : Any, EF : Any> AppCompatActivity.onCreated(
    processor: () -> EffectProcessor<EV, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onEffect, intents()) }

@JvmName("OnCreatedStateProcessor")
fun <EV : Any, ST : Any> ComponentActivity.onCreated(
    processor: () -> StateProcessor<EV, ST>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, intents()) }

@JvmName("OnCreatedStateEffectProcessor")
fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onCreated(
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, onEffect, intents()) }

@JvmName("OnCreatedEffectProcessor")
fun <EV : Any, EF : Any> ComponentActivity.onCreated(
    processor: () -> EffectProcessor<EV, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onEffect, intents()) }

@JvmName("OnStartedStateProcessor")
fun <EV : Any, ST : Any> AppCompatActivity.onStarted(
    processor: () -> StateProcessor<EV, ST>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, intents()) }

fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onStarted(
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, onEffect, intents()) }

@JvmName("OnStartedEffectProcessor")
fun <EV : Any, EF : Any> AppCompatActivity.onStarted(
    processor: () -> EffectProcessor<EV, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onEffect, intents()) }

@JvmName("OnStartedStateProcessor")
fun <EV : Any, ST : Any> ComponentActivity.onStarted(
    processor: () -> StateProcessor<EV, ST>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, intents()) }

@JvmName("OnStartedStateEffectProcessor")
fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onStarted(
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, onEffect, intents()) }

@JvmName("OnStartedEffectProcessor")
fun <EV : Any, EF : Any> ComponentActivity.onStarted(
    processor: () -> EffectProcessor<EV, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onEffect, intents()) }

@JvmName("OnResumedStateProcessor")
fun <EV : Any, ST : Any> AppCompatActivity.onResumed(
    processor: () -> StateProcessor<EV, ST>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, intents()) }

fun <EV : Any, ST : Any, EF : Any> AppCompatActivity.onResumed(
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, onEffect, intents()) }

@JvmName("OnResumedEffectProcessor")
fun <EV : Any, EF : Any> AppCompatActivity.onResumed(
    processor: () -> EffectProcessor<EV, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onEffect, intents()) }

@JvmName("OnResumedStateProcessor")
fun <EV : Any, ST : Any> ComponentActivity.onResumed(
    processor: () -> StateProcessor<EV, ST>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, intents()) }

@JvmName("OnResumedStateEffectProcessor")
fun <EV : Any, ST : Any, EF : Any> ComponentActivity.onResumed(
    processor: () -> StateEffectProcessor<EV, ST, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onState: (ST) -> Unit = {},
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onState, onEffect, intents()) }

@JvmName("OnResumedEffectProcessor")
fun <EV : Any, EF : Any> ComponentActivity.onResumed(
    processor: () -> EffectProcessor<EV, EF>,
    intents: () -> List<Flow<EV>> = { emptyList() },
    onEffect: (EF) -> Unit = {}
) = lifecycleScope.launchWhenCreated { consume(processor(), onEffect, intents()) }
