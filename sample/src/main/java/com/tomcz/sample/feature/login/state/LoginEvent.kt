package com.tomcz.sample.feature.login.state

sealed interface LoginEvent {
    object GoToRegister : LoginEvent
    data class LoginClick(val email: String, val pass: String) : LoginEvent
}
