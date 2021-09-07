package com.tomcz.sample.feature.register.state

sealed interface RegisterEffect {
    object GoToLogin : RegisterEffect
    object GoToHome : RegisterEffect
}
