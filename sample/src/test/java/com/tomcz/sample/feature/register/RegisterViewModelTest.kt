package com.tomcz.sample.feature.register

import com.tomcz.ellipse.test.processorTest
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterState
import com.tomcz.sample.util.BaseCoroutineTest
import org.junit.jupiter.api.Test

internal class RegisterViewModelTest : BaseCoroutineTest() {

    private val viewModel: RegisterViewModel = RegisterViewModel()

    @Test
    fun `test changing email`() = processorTest(
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
