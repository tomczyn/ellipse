package com.tomcz.sample.feature.foo

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.sample.feature.foo.state.FooEffect
import com.tomcz.sample.feature.foo.state.FooEvent
import com.tomcz.sample.feature.foo.state.FooPartial
import com.tomcz.sample.feature.foo.state.FooState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

typealias FooProcessor = Processor<FooEvent, FooState, FooEffect>

class FooViewModel : ViewModel() {

    val processor: FooProcessor = processor(
        initialState = FooState(),
        prepare = {
            effects.send(FooEffect.BarEffect)
            val firstFlow = flow {
                emit(FooPartial.Increase)
                emit(FooPartial.Increase)
                delay(100)
                emit(FooPartial.Increase)
            }
            effects.send(FooEffect.BarEffect)
            delay(100)
            effects.send(FooEffect.BarEffect)
            val secondFlow = flow {
                delay(100)
                emit(FooPartial.Increase)
                emit(FooPartial.Increase)
            }
            merge(firstFlow, secondFlow)
        },
    )
}
