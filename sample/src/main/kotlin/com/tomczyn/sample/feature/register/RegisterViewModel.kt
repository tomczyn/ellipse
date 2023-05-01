package com.tomczyn.sample.feature.register

import androidx.lifecycle.ViewModel
import com.tomczyn.ellipse.Ellipse
import com.tomczyn.ellipse.common.ellipse
import com.tomczyn.ellipse.common.toNoAction
import com.tomczyn.sample.feature.register.state.RegisterEffect
import com.tomczyn.sample.feature.register.state.RegisterEvent
import com.tomczyn.sample.feature.register.state.RegisterPartialState
import com.tomczyn.sample.feature.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

typealias RegisterEllipse = Ellipse<RegisterEvent, RegisterState, RegisterEffect>

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val ellipse: RegisterEllipse = ellipse(
        initialState = RegisterState(),
        prepare = { emptyFlow() },
        onEvent = { event ->
            when (event) {
                is RegisterEvent.EmailChanged -> flowOf(RegisterPartialState.EmailChanged(event.email))
                is RegisterEvent.PasswordChanged -> flowOf(
                    RegisterPartialState.PasswordChanged(event.password)
                )
                is RegisterEvent.RepeatPasswordChanged -> flowOf(
                    RegisterPartialState.RepeatPasswordChanged(event.repeatPassword)
                )
                RegisterEvent.GoToLogin -> {
                    effects.send(RegisterEffect.GoToLogin)
                        .toNoAction()
                }
                RegisterEvent.RegisterClicked -> registerUser()
                    .onCompletion { effects.send(RegisterEffect.GoToHome) }
                    .toNoAction()
            }
        })

    private fun registerUser(): Flow<Unit> = emptyFlow()
}
