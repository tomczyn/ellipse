package com.tomcz.ellipse.internal

import com.tomcz.ellipse.EffectsCollector
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.EllipseContext
import com.tomcz.ellipse.internal.util.reduceAndSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class FlowProcessor<in EV : Any, ST : Any, out PA : PartialState<ST>, EF : Any> constructor(
    private val scope: CoroutineScope,
    private val effectClass: KClass<EF>,
    initialState: ST,
    prepare: suspend EllipseContext<ST, EF>.() -> Flow<PA>,
    private val onEvent: suspend EllipseContext<ST, EF>.(EV) -> Flow<PA>,
) : Processor<EV, ST, EF> {

    override val effect: Flow<EF>
        get() = effect(effectClass)

    private val effectFlows: MutableMap<KClass<out EF>, MutableSharedFlow<Any>> = mutableMapOf(
        effectClass to mutableEffectFlow(effectClass)
    )

    override val state: StateFlow<ST>
        get() = stateFlow
    private val stateFlow: MutableStateFlow<ST> = MutableStateFlow(initialState)

    private val effectCache: MutableMap<KClass<out EF>, MutableList<Any>> = mutableMapOf()

    private val context: EllipseContext<ST, EF>
        get() = EllipseContext(state, effectsCollector)

    private val effectsCollector: EffectsCollector<EF> = object : EffectsCollector<EF> {
        override fun send(vararg effect: EF) {
            scope.launch {
                effect.forEach { effect ->
                    if (effectFlows[effect::class] == null)
                        effectFlows[effect::class] = mutableEffectFlow(effect::class)
                    effectFlows.keys
                        .filter { key -> effect::class.isSubclassOf(key) }
                        .onEach { key -> emitEffect(key, effect) }
                }
            }
        }
    }

    init {
        scope.launch {
            prepare(context).collect { stateFlow.reduceAndSet(it) }
        }
    }

    @OptIn(FlowPreview::class)
    override fun <T : EF> effect(filterClass: KClass<T>): Flow<T> = flow {
        emit(setupEffectFlow(filterClass))
    }.flatMapConcat { it }

    override fun sendEvent(vararg event: EV) {
        scope.launch {
            event.forEach {
                onEvent(context, it).collect { partial -> stateFlow.reduceAndSet(partial) }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T : EF> setupEffectFlow(
        filterClass: KClass<T>
    ): Flow<T> = withContext(scope.coroutineContext) {
        if (effectFlows.contains(filterClass)) {
            effectFlows[filterClass] as Flow<T>
        } else {
            val effectFlow = mutableEffectFlow(filterClass)
            effectFlows[filterClass] = effectFlow
            effectFlow as Flow<T>
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T : EF> MutableSharedFlow<Any>.addCacheObserver(filterClass: KClass<T>): MutableSharedFlow<Any> {
        subscriptionCount
            .flatMapLatest { subscribers ->
                if (subscribers != 0) {
                    while (effectCache[filterClass]?.isNotEmpty() == true) {
                        effectCache[filterClass]
                            ?.removeFirst()
                            ?.let { cachedEffect -> this@addCacheObserver.emit(cachedEffect) }
                    }
                }
                flowOf(Unit)
            }
            .launchIn(scope)
        return this
    }

    private suspend fun emitEffect(key: KClass<out EF>, effect: EF) {
        val flow = effectFlows[key]
        if (flow != null && flow.subscriptionCount.value != 0) {
            flow.emit(effect)
        } else {
            if (effectCache[key] == null) effectCache[key] = mutableListOf()
            effectCache[key]!!.add(effect)
        }
    }

    private fun <T : EF> mutableEffectFlow(effectClass: KClass<T>) =
        MutableSharedFlow<Any>().addCacheObserver(effectClass)
}
