package com.tomcz.sample.feature.login

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.setState
import com.tomcz.sample.feature.login.state.LoginEffect
import com.tomcz.sample.feature.login.state.LoginEvent
import com.tomcz.sample.feature.login.state.LoginPartial
import com.tomcz.sample.feature.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

typealias LoginProcessor = Processor<LoginEvent, LoginState, LoginEffect>

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    val processor: LoginProcessor = processor(initialState = LoginState()) { event ->
        when (event) {
            is LoginEvent.LoginClick -> {
                setState(LoginPartial.ShowLoading)
                val isSuccess = loginUser(event.email, event.pass)
                setState(LoginPartial.HideLoading)
                if (isSuccess) {
                    sendEffect(LoginEffect.GoToHome)
                } else {
                    sendEffect(LoginEffect.ShowError)
                }
            }
            LoginEvent.GoToRegister -> sendEffect(LoginEffect.GoToRegister)
        }
    }

    private suspend fun loginUser(email: String, pass: String): Boolean {
        delay(2000)
        return email == "a" && pass == "b"
    }
}
