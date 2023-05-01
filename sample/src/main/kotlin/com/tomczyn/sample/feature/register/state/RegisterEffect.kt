package com.tomczyn.sample.feature.register.state

sealed interface RegisterEffect {
    object GoToLogin : RegisterEffect
    object GoToHome : RegisterEffect
}
