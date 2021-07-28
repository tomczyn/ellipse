package com.tomcz.sample.register

import androidx.lifecycle.ViewModel
import com.tomcz.mvi.StateEffectProcessor
import com.tomcz.mvi.common.stateEffectProcessor
import com.tomcz.sample.register.state.RegisterEffect
import com.tomcz.sample.register.state.RegisterEvent
import com.tomcz.sample.register.state.RegisterPartialState
import com.tomcz.sample.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    val processor: StateEffectProcessor<RegisterEvent, RegisterState, RegisterEffect> =
        stateEffectProcessor(
            defViewState = RegisterState(),
            prepare = { effects ->
                effects.send(RegisterEffect.Init)
                emptyFlow()
            },
            statesEffects = { _, event ->
                when (event) {
                    is RegisterEvent.EmailChanged -> flowOf(RegisterPartialState.EmailChanged(event.email))
                    is RegisterEvent.PasswordChanged -> flowOf(
                        RegisterPartialState.PasswordChanged(event.password)
                    )
                    is RegisterEvent.RepeatPasswordChanged -> flowOf(
                        RegisterPartialState.RepeatPasswordChanged(event.repeatPassword)
                    )
                }
            })
}
