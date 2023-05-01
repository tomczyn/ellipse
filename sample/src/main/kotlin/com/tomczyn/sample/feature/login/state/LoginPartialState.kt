package com.tomczyn.sample.feature.login.state

import com.tomczyn.ellipse.PartialState

sealed interface LoginPartialState : PartialState<LoginState> {
    object ShowLoading : LoginPartialState {
        override fun reduce(oldState: LoginState): LoginState = oldState.copy(isLoading = true)
    }

    object HideLoading : LoginPartialState {
        override fun reduce(oldState: LoginState): LoginState = oldState.copy(isLoading = true)
    }
}
