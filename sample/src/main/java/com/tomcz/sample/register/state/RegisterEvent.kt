package com.tomcz.sample.register.state

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data class RepeatPasswordChanged(val repeatPassword: String) : RegisterEvent()
}

