package com.tomcz.sample.feature.login.state

import com.tomcz.ellipse.Partial

sealed interface LoginPartial : Partial<LoginState> {
    object ShowLoading : LoginPartial {
        override fun reduce(oldState: LoginState): LoginState = oldState.copy(isLoading = true)
    }

    object HideLoading : LoginPartial {
        override fun reduce(oldState: LoginState): LoginState = oldState.copy(isLoading = true)
    }
}
