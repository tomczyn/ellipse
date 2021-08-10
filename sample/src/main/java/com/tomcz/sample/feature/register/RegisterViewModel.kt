package com.tomcz.sample.feature.register

import androidx.lifecycle.ViewModel
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.common.stateEffectProcessor
import com.tomcz.sample.feature.register.state.RegisterEffect
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterPartialState
import com.tomcz.sample.feature.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val processor: StateEffectProcessor<RegisterEvent, RegisterState, RegisterEffect> =
        stateEffectProcessor(
            initialState = RegisterState(),
            prepare = { emptyFlow() },
            statesEffects = { effects, event ->
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
                        emptyFlow()
                    }
                }
            })
}
