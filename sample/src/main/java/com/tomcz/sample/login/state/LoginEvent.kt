package com.tomcz.sample.login.state

sealed interface LoginEvent {
    data class LoginClick(val email: String, val pass: String) : LoginEvent
}
