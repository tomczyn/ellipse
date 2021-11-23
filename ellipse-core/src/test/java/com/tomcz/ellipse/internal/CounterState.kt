package com.tomcz.ellipse.internal

import com.tomcz.ellipse.PartialState

object CounterEvent

object CounterEffect

data class CounterState(val counter: Int = 0)

object IncreasePartialState : PartialState<CounterState> {
    override fun reduce(oldState: CounterState): CounterState =
        oldState.copy(counter = oldState.counter + 1)
}
