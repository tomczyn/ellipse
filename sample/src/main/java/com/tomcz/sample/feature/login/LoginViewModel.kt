package com.tomcz.sample.feature.login

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.toNoAction
import com.tomcz.sample.feature.login.state.LoginEffect
import com.tomcz.sample.feature.login.state.LoginEvent
import com.tomcz.sample.feature.login.state.LoginPartialState
import com.tomcz.sample.feature.login.state.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

typealias LoginProcessor = Processor<LoginEvent, LoginState, LoginEffect>

class LoginViewModel : ViewModel() {

    val processor: LoginProcessor = processor(initialState = LoginState()) { event ->
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
            LoginEvent.GoToRegister -> effects.send(LoginEffect.GoToRegister).toNoAction()
        }
    }

    private suspend fun loginUser(email: String, pass: String): Boolean {
        delay(2000)
        return true
    }
}
