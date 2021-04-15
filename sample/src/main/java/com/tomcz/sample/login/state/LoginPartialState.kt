package com.tomcz.sample.login.state

import com.tomcz.mvi.Intent

sealed class LoginPartialState : Intent<LoginState> {

    data class EmailChanged(val email: String) : LoginPartialState() {
        override fun reduce(oldState: LoginState): LoginState {
            return oldState.copy(email = email)
        }
    }

    data class PasswordChanged(val password: String) : LoginPartialState() {
        override fun reduce(oldState: LoginState): LoginState {
            return oldState.copy(password = password)
        }
    }
}
