package com.tomcz.sample.feature.register

import androidx.lifecycle.ViewModel
import com.tomcz.ellipse.PartialState
import com.tomcz.ellipse.Processor
import com.tomcz.ellipse.common.processor
import com.tomcz.ellipse.common.toNoAction
import com.tomcz.sample.feature.register.state.RegisterEffect
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterPartial
import com.tomcz.sample.feature.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

typealias RegisterProcessor = Processor<RegisterEvent, RegisterState, RegisterEffect>

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val processor: RegisterProcessor = processor(
        initialState = RegisterState(),
        prepare = { emptyFlow() },
        onEvent = { event ->
            when (event) {
                is RegisterEvent.EmailChanged -> flowOf(RegisterPartial.EmailChanged(event.email))
                is RegisterEvent.PasswordChanged -> flowOf(
                    RegisterPartial.PasswordChanged(event.password)
                )
                is RegisterEvent.RepeatPasswordChanged -> flowOf(
                    RegisterPartial.RepeatPasswordChanged(event.repeatPassword)
                )
                RegisterEvent.GoToLogin -> {
                    effects.send(RegisterEffect.GoToLogin)
                        .toNoAction()
                }
                RegisterEvent.RegisterClicked -> registerUser()
                    .onCompletion { effects.send(RegisterEffect.GoToHome) }
                RegisterEvent.HideBlind -> flowOf(RegisterPartial.HideBlind)
            }
        })

    private fun registerUser(): Flow<PartialState<RegisterState>> =
        flowOf<PartialState<RegisterState>>()
            .catch { emit(RegisterPartial.ShowBlind("Error")) }
}
