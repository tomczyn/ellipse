package com.tomcz.sample.feature.login

import androidx.lifecycle.ViewModel
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.common.stateEffectProcessor
import com.tomcz.sample.feature.login.state.LoginEffect
import com.tomcz.sample.feature.login.state.LoginEvent
import com.tomcz.sample.feature.login.state.LoginPartialState
import com.tomcz.sample.feature.login.state.LoginState
import com.tomcz.sample.util.thenNoAction
import kotlinx.coroutines.flow.flow

class LoginViewModel : ViewModel() {

    val processor: StateEffectProcessor<LoginEvent, LoginState, LoginEffect> =
        stateEffectProcessor(initialState = LoginState()) { effects, event ->
            when (event) {
                is LoginEvent.LoginClick -> flow {
                    emit(LoginPartialState.ShowLoading)
                    val isSuccess = loginUser(event.email, event.pass)
                    emit(LoginPartialState.HideLoading)
                    if (isSuccess) {
                        effects.send(LoginEffect.GoToHome)
                    } else {
                        effects.send(LoginEffect.ShowError)
                    }
                }
                LoginEvent.GoToRegister -> effects
                    .send(LoginEffect.GoToRegister).thenNoAction()
            }
        }

    private suspend fun loginUser(email: String, pass: String): Boolean = true
}
