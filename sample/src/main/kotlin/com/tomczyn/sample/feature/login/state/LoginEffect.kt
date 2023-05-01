package com.tomczyn.sample.feature.login.state

sealed interface LoginEffect {
    object GoToHome : LoginEffect
    object GoToRegister : LoginEffect
    object ShowError : LoginEffect
}
