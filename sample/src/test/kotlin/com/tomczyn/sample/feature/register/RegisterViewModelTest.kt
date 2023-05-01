package com.tomczyn.sample.feature.register

import com.tomczyn.ellipse.test.ellipseTest
import com.tomczyn.sample.feature.register.state.RegisterEvent
import com.tomczyn.sample.feature.register.state.RegisterState
import com.tomczyn.sample.feature.common.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class RegisterViewModelTest : BaseCoroutineTest() {

    private val viewModel: RegisterViewModel by lazy { RegisterViewModel() }

    @Test
    fun `test prepare`() = ellipseTest(
        ellipse = { viewModel.ellipse },
        given = { /* Setup */ },
        thenStates = { assertValues(RegisterState()) }
    )

    @Test
    fun `test changing email`() = ellipseTest(
        ellipse = { viewModel.ellipse },
        given = { /* Setup */ },
        whenEvent = RegisterEvent.EmailChanged("test@test.test"),
        thenStates = { assertLast(RegisterState("test@test.test")) }
    )
}
