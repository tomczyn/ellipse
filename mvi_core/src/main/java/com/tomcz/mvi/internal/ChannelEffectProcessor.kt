package com.tomcz.mvi.internal

import com.tomcz.mvi.EffectProcessor
import com.tomcz.mvi.Effects
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class ChannelEffectProcessor<in EV : Any, EF : Any>(
    private val scope: CoroutineScope,
    prepare: (suspend (Effects<EF>) -> Unit)? = null,
    private val mapper: suspend (Effects<EF>, EV) -> Unit,
) : EffectProcessor<EV, EF> {

    override val effect: Flow<EF>
        get() = _effect.receiveAsFlow()
    private val _effect: Channel<EF> = Channel()

    private val effects: Effects<EF> = object : Effects<EF> {
        override fun send(effect: EF) {
            scope.launch { _effect.send(effect) }
        }
    }

    init {
        prepare?.let { scope.launch { it(effects) } }
    }

    override fun process(event: EV) {
        scope.launch { mapper(effects, event) }
    }
}
