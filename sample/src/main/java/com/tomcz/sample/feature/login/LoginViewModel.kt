package com.tomcz.sample.feature.login

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.sample.feature.login.state.LoginEffect
import com.tomcz.sample.feature.login.state.LoginEvent
import com.tomcz.sample.feature.login.state.LoginPartialState
import com.tomcz.sample.feature.login.state.LoginState
import com.tomcz.sample.util.toNoAction
import kotlinx.coroutines.flow.flow

class LoginViewModel : ViewModel() {

    val processor: Processor<LoginEvent, LoginState, LoginEffect> =
        processor(initialState = LoginState()) { event ->
            when (event) {
                is LoginEvent.LoginClick -> flow {
                    emit(LoginPartialState.ShowLoading)
                    val isSuccess = loginUser(event.email, event.pass)
                    emit(LoginPartialState.HideLoading)
                    if (isSuccess) {
                        sendEffect(LoginEffect.GoToHome)
                    } else {
                        sendEffect(LoginEffect.ShowError)
                    }
                }
                LoginEvent.GoToRegister -> sendEffect(LoginEffect.GoToRegister).toNoAction()
            }
        }

    private suspend fun loginUser(email: String, pass: String): Boolean = true
}
