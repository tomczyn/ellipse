package com.tomcz.sample.feature.foo.state

import com.tomcz.ellipse.PartialState

sealed interface FooPartial : PartialState<FooState> {

    object Increase : FooPartial {
        override fun reduce(oldState: FooState): FooState =
            oldState.copy(number = oldState.number + 1)
    }

    object Decrease : FooPartial {
        override fun reduce(oldState: FooState): FooState =
            oldState.copy(number = oldState.number - 1)
    }
}
