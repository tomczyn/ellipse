package com.tomcz.sample.feature.foo

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.EllipseContext
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.NoAction
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.setState
import com.tomcz.sample.common.AndroidDispatcherProvider
import com.tomcz.sample.common.DispatcherProvider
import com.tomcz.sample.common.emitAbort
import com.tomcz.sample.common.onCancel
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

typealias FooProcessor = Processor<FooEvent, FooState, FooEffect>
typealias FooContext = EllipseContext<FooState, FooEffect>

@FlowPreview
class FooViewModel(
    dispatchers: DispatcherProvider = AndroidDispatcherProvider
) : ViewModel() {

    private val bar: Bar = Bar(dispatchers)

    val processor: FooProcessor = processor(
        initialState = FooState(),
        prepare = {
            sendEffect(BarEffect)
            firstPrepareFlow()
            sendEffect(BarEffect)
            delay(100)
            sendEffect(BarEffect)
            secondPrepareFlow()
            onCancel { bar.close() }
        },
        onEvent = { event ->
            when (event) {
                FirstButtonClick -> setState(Increase)
                SecondButtonClick -> setState(NoAction())
                ThirdButtonClick -> sendEffect(BarEffect)
                FourthButtonClick -> fourthClick()
            }
        }
    )

    private fun FooContext.firstPrepareFlow() {
        setState(flow {
            emit(Increase) // 1
            emit(Decrease) // 0
            delay(100)
            emit(Increase) // 1
        })
    }

    private fun FooContext.secondPrepareFlow() {
        setState(flow {
            delay(100)
            emit(Increase) // 2
            emit(Decrease) // 1
        })
    }

    private fun FooContext.fourthClick() {
        setState(bar.infiniteStream().flatMapConcat { condition ->
            if (condition) {
                flow { emitAbort(Decrease) }
            } else {
                flowOf(NoAction())
            }
        })
    }
}
