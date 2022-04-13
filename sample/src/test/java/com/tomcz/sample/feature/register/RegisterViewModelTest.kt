package com.tomcz.sample.feature.register

import com.tomcz.ellipse.test.processorTest
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterState
import com.tomcz.sample.feature.register.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest : BaseCoroutineTest() {

    private val viewModel: RegisterViewModel by lazy { RegisterViewModel() }

    @Test
    fun `test prepare`() = processorTest(
        processor = { viewModel.processor },
        given = { /* Setup */ },
        thenStates = { assertValues(RegisterState()) }
    )

    @Test
    fun `test changing email`() = processorTest(
        processor = { viewModel.processor },
        given = { /* Setup */ },
        whenEvent = RegisterEvent.EmailChanged("test@test.test"),
        thenStates = { assertLast(RegisterState("test@test.test")) }
    )
}
