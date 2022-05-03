package com.tomcz.sample.feature.register

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.setState
import com.tomcz.sample.feature.register.state.RegisterEffect
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterPartial.EmailChanged
import com.tomcz.sample.feature.register.state.RegisterPartial.PasswordChanged
import com.tomcz.sample.feature.register.state.RegisterPartial.RepeatPasswordChanged
import com.tomcz.sample.feature.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

typealias RegisterProcessor = Processor<RegisterEvent, RegisterState, RegisterEffect>

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val processor: RegisterProcessor = processor(
        initialState = RegisterState(),
        prepare = { },
        onEvent = { event ->
            state = state.copy(email = "")
            when (event) {
                is RegisterEvent.EmailChanged -> setState(EmailChanged(event.email))
                is RegisterEvent.PasswordChanged -> setState(flowOf(PasswordChanged(event.password)))
                is RegisterEvent.RepeatPasswordChanged -> setState(RepeatPasswordChanged(event.repeatPassword))
                RegisterEvent.GoToLogin -> sendEffect(RegisterEffect.GoToLogin)
                RegisterEvent.RegisterClicked -> registerUser()
                    .onCompletion { sendEffect(RegisterEffect.GoToHome) }
                    .collect()
            }
        })

    private fun registerUser(): Flow<Unit> = emptyFlow()
}
