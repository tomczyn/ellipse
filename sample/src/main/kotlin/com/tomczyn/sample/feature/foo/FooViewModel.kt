package com.tomczyn.sample.feature.foo

import androidx.lifecycle.ViewModel
import com.tomczyn.ellipse.Processor
import com.tomczyn.ellipse.common.NoAction
import com.tomczyn.ellipse.common.processor
import com.tomczyn.ellipse.common.toNoAction
import com.tomczyn.sample.common.AndroidDispatcherProvider
import com.tomczyn.sample.common.DispatcherProvider
import com.tomczyn.sample.feature.foo.state.FooEffect
import com.tomczyn.sample.feature.foo.state.FooEffect.BarEffect
import com.tomczyn.sample.feature.foo.state.FooEvent
import com.tomczyn.sample.feature.foo.state.FooEvent.FirstButtonClick
import com.tomczyn.sample.feature.foo.state.FooEvent.FourthButtonClick
import com.tomczyn.sample.feature.foo.state.FooEvent.SecondButtonClick
import com.tomczyn.sample.feature.foo.state.FooEvent.ThirdButtonClick
import com.tomczyn.sample.feature.foo.state.FooPartial.Decrease
import com.tomczyn.sample.feature.foo.state.FooPartial.Increase
import com.tomczyn.sample.feature.foo.state.FooState
import com.tomczyn.sample.common.emitAbort
import com.tomczyn.sample.common.onCancel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge

typealias FooProcessor = Processor<FooEvent, FooState, FooEffect>

@FlowPreview
class FooViewModel(
    dispatchers: DispatcherProvider = AndroidDispatcherProvider
) : ViewModel() {

    private val bar: Bar = Bar(dispatchers)

    val processor: FooProcessor = processor(
        initialState = FooState(),
        prepare = {
            effects.send(BarEffect)
            val firstFlow = flow {
                emit(Increase) // 1
                emit(Decrease) // 0
                delay(100)
                emit(Increase) // 1
            }
            effects.send(BarEffect)
            delay(100)
            effects.send(BarEffect)
            val secondFlow = flow {
                delay(100)
                emit(Increase) // 2
                emit(Decrease) // 1
            }
            merge(
                onCancel { bar.close() },
                firstFlow,
                secondFlow
            )
        },
        onEvent = { event ->
            when (event) {
                FirstButtonClick -> flowOf(Increase)
                SecondButtonClick -> flowOf(NoAction())
                ThirdButtonClick -> effects.send(BarEffect).toNoAction()
                FourthButtonClick -> bar.infiniteStream().flatMapConcat { condition ->
                    if (condition) {
                        flow { emitAbort(Decrease) }
                    } else {
                        flowOf(NoAction())
                    }
                }
            }
        }
    )
}
