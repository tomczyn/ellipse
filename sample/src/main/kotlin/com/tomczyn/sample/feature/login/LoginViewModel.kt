package com.tomczyn.sample.feature.login

import androidx.lifecycle.ViewModel
import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.common.ellipse
import com.tomczyn.ellipse.common.toNoAction
import com.tomczyn.sample.feature.login.state.LoginEffect
import com.tomczyn.sample.feature.login.state.LoginEvent
import com.tomczyn.sample.feature.login.state.LoginPartialState
import com.tomczyn.sample.feature.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias LoginEllipse = Ellipse<LoginEvent, LoginState, LoginEffect>

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    val ellipse: LoginEllipse = ellipse(initialState = LoginState()) { event ->
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
        return email == pass
    }
}
