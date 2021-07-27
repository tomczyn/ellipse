package com.tomcz.sample.register.state

import com.tomcz.mvi.PartialState

sealed class RegisterPartialState : PartialState<RegisterState> {

    data class EmailChanged(val email: String) : RegisterPartialState() {
        override fun reduce(oldState: RegisterState): RegisterState {
            return oldState.copy(email = email)
        }
    }

    data class PasswordChanged(val password: String) : RegisterPartialState() {
        override fun reduce(oldState: RegisterState): RegisterState {
            return oldState.copy(password = password)
        }
    }

    data class RepeatPasswordChanged(val repeatPassword: String) : RegisterPartialState() {
        override fun reduce(oldState: RegisterState): RegisterState {
            return oldState.copy(repeatPassword = repeatPassword)
        }
    }
}
