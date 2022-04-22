package com.tomcz.sample.feature.foo

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.NoAction
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.toNoAction
import com.tomcz.sample.common.AndroidDispatcherProvider
import com.tomcz.sample.common.DispatcherProvider
import com.tomcz.sample.feature.foo.state.FooEffect
import com.tomcz.sample.feature.foo.state.FooEffect.BarEffect
import com.tomcz.sample.feature.foo.state.FooEvent
import com.tomcz.sample.feature.foo.state.FooEvent.FirstButtonClick
import com.tomcz.sample.feature.foo.state.FooEvent.FourthButtonClick
import com.tomcz.sample.feature.foo.state.FooEvent.SecondButtonClick
import com.tomcz.sample.feature.foo.state.FooEvent.ThirdButtonClick
import com.tomcz.sample.feature.foo.state.FooPartial.Decrease
import com.tomcz.sample.feature.foo.state.FooPartial.Increase
import com.tomcz.sample.feature.foo.state.FooState
import com.tomcz.sample.common.emitAbort
import com.tomcz.sample.common.onCancel
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
