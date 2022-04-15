package com.tomcz.sample.feature.foo.state

sealed interface FooEvent {
    object FirstButtonClick : FooEvent
    object SecondButtonClick : FooEvent
    object ThirdButtonClick : FooEvent
    object FourthButtonClick : FooEvent
}
