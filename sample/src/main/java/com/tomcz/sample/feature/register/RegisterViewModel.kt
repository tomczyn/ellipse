package com.tomcz.sample.feature.register

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.sample.feature.register.state.RegisterEffect
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterPartialState
import com.tomcz.sample.feature.register.state.RegisterState
import com.tomcz.sample.util.toNoAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val processor: Processor<RegisterEvent, RegisterState, RegisterEffect> = processor(
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
                RegisterEvent.GoToLogin -> sendEffect(RegisterEffect.GoToLogin)
                    .toNoAction()
                RegisterEvent.RegisterClicked -> registerUser()
                    .onCompletion { sendEffect(RegisterEffect.GoToHome) }
                    .toNoAction()
            }
        })

    private fun registerUser(): Flow<Unit> = emptyFlow()
}
