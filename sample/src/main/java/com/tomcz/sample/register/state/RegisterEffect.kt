package com.tomcz.sample.register.state

sealed interface RegisterEffect {
    object Init : RegisterEffect
}
