package com.tomcz.ellipse.internal

import com.tomcz.ellipse.PartialState

sealed interface CounterEvent {
    object Event1 : CounterEvent
    object Event2 : CounterEvent
}

sealed interface CounterEffect {
    object Effect1 : CounterEffect
    object Effect2 : CounterEffect
}

data class CounterState(val counter: Int = 0)

object IncreasePartialState : PartialState<CounterState> {
    override fun reduce(oldState: CounterState): CounterState =
        oldState.copy(counter = oldState.counter + 1)
}
