package com.tomcz.sample.feature.register.state

sealed interface RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent
    data class PasswordChanged(val password: String) : RegisterEvent
    data class RepeatPasswordChanged(val repeatPassword: String) : RegisterEvent
    object GoToLogin : RegisterEvent
}

