package com.tomcz.sample.login

import androidx.lifecycle.ViewModel
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.common.stateEffectProcessor
import com.tomcz.sample.login.state.LoginEffect
import com.tomcz.sample.login.state.LoginEvent
import com.tomcz.sample.login.state.LoginPartialState
import com.tomcz.sample.login.state.LoginState
import kotlinx.coroutines.flow.flow

class LoginViewModel : ViewModel() {

    val processor: StateEffectProcessor<LoginEvent, LoginState, LoginEffect> =
        stateEffectProcessor(initialState = LoginState()) { effects, event ->
            when (event) {
                is LoginEvent.LoginClick -> flow {
                    emit(LoginPartialState.ShowLoading)
                    val isSuccess = loginUser(event.email, event.pass)
                    emit(LoginPartialState.HideLoading)
                    if (isSuccess) effects.send(LoginEffect.GoToHome)
                    else effects.send(LoginEffect.ShowError)
                }
            }
        }

    private fun loginUser(email: String, pass: String): Boolean = TODO()
}
