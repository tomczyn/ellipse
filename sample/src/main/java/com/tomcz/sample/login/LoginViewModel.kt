package com.tomcz.sample.login

import androidx.lifecycle.ViewModel
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.common.stateEffectProcessor
import com.tomcz.sample.login.state.LoginEffect
import com.tomcz.sample.login.state.LoginEvent
import com.tomcz.sample.login.state.LoginPartialState.EmailChanged
import com.tomcz.sample.login.state.LoginPartialState.PasswordChanged
import com.tomcz.sample.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    val processor: StateEffectProcessor<LoginEvent, LoginState, LoginEffect> =
        stateEffectProcessor(
            defViewState = LoginState(),
            prepare = { emptyFlow() },
            statesEffects = { _, event ->
                when (event) {
                    is LoginEvent.EmailChanged -> flowOf(EmailChanged(event.email))
                    is LoginEvent.PasswordChanged -> flowOf(PasswordChanged(event.password))
                }
            })
}