package com.tomcz.sample.feature.register

import com.tomcz.ellipse.test.processorTest
import com.tomcz.sample.feature.register.state.RegisterEvent
import com.tomcz.sample.feature.register.state.RegisterState
import com.tomcz.sample.util.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class RegisterViewModelTest : BaseCoroutineTest() {

    /*
     We can test processor's prepare method only when viewModel is created lazily.
     For example with lazy delegate or in the function. We can't create RegisterViewModel right away
     */
    private val viewModel: RegisterViewModel by lazy { RegisterViewModel() }

    @Test
    fun `test prepare`() = processorTest(
        processor = { viewModel.processor },
        given = { /* Setup */ },
        thenStates = {
            assertValues(RegisterState())
        },
    )

    @Test
    fun `test changing email`() = processorTest(
        processor = { viewModel.processor },
        given = { /* Setup */ },
        whenEvent = RegisterEvent.EmailChanged("test@test.test"),
        thenStates = {
            assertLast(RegisterState("test@test.test"))
        },
    )
}
