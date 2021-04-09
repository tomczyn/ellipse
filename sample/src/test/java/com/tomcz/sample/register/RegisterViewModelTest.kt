package com.tomcz.sample.register

import com.tomcz.mvi.test.BaseCoroutineTest
import com.tomcz.mvi.test.processorTest
import com.tomcz.sample.register.state.RegisterEvent
import com.tomcz.sample.register.state.RegisterState
import org.junit.jupiter.api.Test

internal class RegisterViewModelTest : BaseCoroutineTest() {

    private val viewModel: RegisterViewModel = RegisterViewModel()

    @Test
    fun `test changing email`() {
        processorTest(
            given = viewModel::processor,
            whenEvent = RegisterEvent.EmailChanged("test@test.test"),
            thenStates = {
                assertValues(
                    RegisterState(),
                    RegisterState("test@test.test")
                )
            }
        )
    }

}
