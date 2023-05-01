package com.tomczyn.sample.feature.foo.state

sealed interface FooEffect {
    object BarEffect : FooEffect
}
