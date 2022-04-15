package com.tomcz.sample.feature.foo.state

sealed interface FooEffect {

    object BarEffect : FooEffect
}
