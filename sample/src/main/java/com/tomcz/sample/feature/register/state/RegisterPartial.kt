package com.tomcz.sample.feature.register.state

import com.tomcz.ellipse.Partial

sealed interface RegisterPartial : Partial<RegisterState> {

    data class EmailChanged(val email: String) : RegisterPartial {
        override fun reduce(oldState: RegisterState): RegisterState {
            return oldState.copy(email = email)
        }
    }

    data class PasswordChanged(val password: String) : RegisterPartial {
        override fun reduce(oldState: RegisterState): RegisterState {
            return oldState.copy(password = password)
        }
    }

    data class RepeatPasswordChanged(val repeatPassword: String) : RegisterPartial {
        override fun reduce(oldState: RegisterState): RegisterState {
            return oldState.copy(repeatPassword = repeatPassword)
        }
    }
}
